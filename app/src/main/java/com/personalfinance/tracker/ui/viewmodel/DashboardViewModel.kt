package com.personalfinance.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalfinance.tracker.data.model.*
import com.personalfinance.tracker.data.repository.PersonalFinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class DashboardUiState(
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val savingsRate: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val budgetStatus: List<BudgetWithSpending> = emptyList(),
    val upcomingBills: List<BillReminderWithStatus> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PersonalFinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadDashboardData() {
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
                
                // Get monthly income and expenses
                val monthlyIncome = repository.getTotalAmountByTypeAndDateRange(
                    TransactionType.INCOME, monthStart, monthEnd
                ) ?: 0.0
                
                val monthlyExpenses = repository.getTotalAmountByTypeAndDateRange(
                    TransactionType.EXPENSE, monthStart, monthEnd
                ) ?: 0.0
                
                // Calculate total balance (simplified: total income - total expenses)
                val totalBalance = monthlyIncome - monthlyExpenses
                
                // Calculate savings rate
                val savingsRate = if (monthlyIncome > 0) {
                    ((monthlyIncome - monthlyExpenses) / monthlyIncome) * 100
                } else 0.0
                
                // Get recent transactions
                val recentTransactionsFlow = repository.getRecentTransactions(5)
                val recentTransactions = mutableListOf<Transaction>()
                recentTransactionsFlow.collect { transactionList ->
                    recentTransactions.addAll(transactionList)
                }
                
                // Get budget status
                val budgetsFlow = repository.getBudgetsForMonth(currentMonth, currentYear)
                val budgets = mutableListOf<Budget>()
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
                
                // Get upcoming bills
                val upcomingBills = repository.getUpcomingBills()
                
                _uiState.value = _uiState.value.copy(
                    totalBalance = totalBalance,
                    monthlyIncome = monthlyIncome,
                    monthlyExpenses = monthlyExpenses,
                    savingsRate = savingsRate,
                    recentTransactions = recentTransactions,
                    budgetStatus = budgetStatus,
                    upcomingBills = upcomingBills,
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
}
