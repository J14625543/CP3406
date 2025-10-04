package com.personalfinance.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalfinance.tracker.data.repository.PersonalFinanceRepository
import com.personalfinance.tracker.ui.components.Recommendation
import com.personalfinance.tracker.ui.components.RecommendationPriority
import com.personalfinance.tracker.ui.components.RecommendationType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class AnalyticsUiState(
    val recommendations: List<Recommendation> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val repository: PersonalFinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    fun loadAnalyticsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val recommendations = generateRecommendations()
                
                _uiState.value = _uiState.value.copy(
                    recommendations = recommendations,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    private suspend fun generateRecommendations(): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1
        val currentYear = calendar.get(Calendar.YEAR)
        
        // Calculate start and end dates of current month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val monthStart = calendar.time
        
        calendar.add(Calendar.MONTH, 1)
        calendar.add(Calendar.MILLISECOND, -1)
        val monthEnd = calendar.time
        
        // Get monthly expense data
        val monthlyExpenses = repository.getTotalAmountByTypeAndDateRange(
            com.personalfinance.tracker.data.model.TransactionType.EXPENSE, monthStart, monthEnd
        ) ?: 0.0
        
        val monthlyIncome = repository.getTotalAmountByTypeAndDateRange(
            com.personalfinance.tracker.data.model.TransactionType.INCOME, monthStart, monthEnd
        ) ?: 0.0
        
        // Get budget data
        val budgetsFlow = repository.getBudgetsForMonth(currentMonth, currentYear)
        val budgets = mutableListOf<com.personalfinance.tracker.data.model.Budget>()
        budgetsFlow.collect { budgetList ->
            budgets.addAll(budgetList)
        }
        
        // Generate recommendations
        if (monthlyExpenses > monthlyIncome * 0.8) {
            recommendations.add(
                Recommendation(
                    title = "High Spending",
                    description = "Monthly expenses exceed 80% of income. Consider reducing unnecessary expenses.",
                    type = RecommendationType.SPENDING,
                    priority = RecommendationPriority.HIGH
                )
            )
        }
        
        if (monthlyIncome > 0 && (monthlyIncome - monthlyExpenses) / monthlyIncome < 0.1) {
            recommendations.add(
                Recommendation(
                    title = "Low Savings Rate",
                    description = "Consider increasing savings rate to at least 10%",
                    type = RecommendationType.SAVINGS,
                    priority = RecommendationPriority.MEDIUM
                )
            )
        }
        
        // Check budget overruns
        budgets.forEach { budget ->
            val spentAmount = repository.getTotalAmountByCategoryAndDateRange(
                budget.category, monthStart, monthEnd
            ) ?: 0.0
            
            if (spentAmount > budget.monthlyLimit) {
                recommendations.add(
                    Recommendation(
                        title = "${budget.category} Budget Exceeded",
                        description = "Exceeded budget of ${budget.monthlyLimit}. Consider controlling expenses in this category.",
                        type = RecommendationType.BUDGET,
                        priority = RecommendationPriority.HIGH
                    )
                )
            } else if (spentAmount > budget.monthlyLimit * 0.8) {
                recommendations.add(
                    Recommendation(
                        title = "${budget.category} Budget Near Limit",
                        description = "Used 80% of budget. Please control expenses.",
                        type = RecommendationType.BUDGET,
                        priority = RecommendationPriority.MEDIUM
                    )
                )
            }
        }
        
        // Check savings goals
        val activeGoalsFlow = repository.getActiveGoals()
        val activeGoals = mutableListOf<com.personalfinance.tracker.data.model.SavingsGoal>()
        activeGoalsFlow.collect { goalList ->
            activeGoals.addAll(goalList)
        }
        
        activeGoals.forEach { goal ->
            if (!goal.isOnTrack && !goal.isCompleted) {
                recommendations.add(
                    Recommendation(
                        title = "${goal.name} Behind Schedule",
                        description = "Savings goal is behind schedule. Consider increasing savings or adjusting target.",
                        type = RecommendationType.GOAL,
                        priority = RecommendationPriority.MEDIUM
                    )
                )
            }
        }
        
        return recommendations
    }
}
