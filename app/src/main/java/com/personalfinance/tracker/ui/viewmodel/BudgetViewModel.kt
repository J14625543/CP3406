package com.personalfinance.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalfinance.tracker.data.model.BudgetWithSpending
import com.personalfinance.tracker.data.repository.PersonalFinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class BudgetUiState(
    val budgets: List<BudgetWithSpending> = emptyList(),
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val totalRemaining: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: PersonalFinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    fun loadBudgets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
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
                
                val budgetsFlow = repository.getBudgetsForMonth(currentMonth, currentYear)
                val budgets = mutableListOf<com.personalfinance.tracker.data.model.Budget>()
                budgetsFlow.collect { budgetList ->
                    budgets.addAll(budgetList)
                }
                
                val budgetStatus = budgets.map { budget ->
                    val spentAmount = repository.getTotalAmountByCategoryAndDateRange(
                        budget.category, monthStart, monthEnd
                    ) ?: 0.0
                    val remainingAmount = budget.monthlyLimit - spentAmount
                    val percentageUsed = if (budget.monthlyLimit > 0) {
                        (spentAmount / budget.monthlyLimit) * 100
                    } else 0.0
                    
                    BudgetWithSpending(budget, spentAmount, remainingAmount, percentageUsed)
                }
                
                val totalBudget = budgetStatus.sumOf { it.budget.monthlyLimit }
                val totalSpent = budgetStatus.sumOf { it.spentAmount }
                val totalRemaining = totalBudget - totalSpent
                
                _uiState.value = _uiState.value.copy(
                    budgets = budgetStatus,
                    totalBudget = totalBudget,
                    totalSpent = totalSpent,
                    totalRemaining = totalRemaining,
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

    fun addBudget(category: String, monthlyLimit: Double) {
        viewModelScope.launch {
            try {
                val calendar = Calendar.getInstance()
                val currentMonth = calendar.get(Calendar.MONTH) + 1
                val currentYear = calendar.get(Calendar.YEAR)
                
                val budget = com.personalfinance.tracker.data.model.Budget(
                    category = category,
                    monthlyLimit = monthlyLimit,
                    currentMonth = currentMonth,
                    currentYear = currentYear
                )
                
                repository.insertBudget(budget)
                loadBudgets() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateBudget(budget: com.personalfinance.tracker.data.model.Budget) {
        viewModelScope.launch {
            try {
                repository.updateBudget(budget)
                loadBudgets() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteBudget(budget: com.personalfinance.tracker.data.model.Budget) {
        viewModelScope.launch {
            try {
                repository.deleteBudget(budget)
                loadBudgets() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
