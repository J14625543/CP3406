package com.personalfinance.tracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalfinance.tracker.data.model.SavingsGoal
import com.personalfinance.tracker.data.repository.PersonalFinanceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GoalsUiState(
    val activeGoals: List<SavingsGoal> = emptyList(),
    val completedGoals: List<SavingsGoal> = emptyList(),
    val totalTargetAmount: Double = 0.0,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val repository: PersonalFinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalsUiState())
    val uiState: StateFlow<GoalsUiState> = _uiState.asStateFlow()

    fun loadGoals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val activeGoalsFlow = repository.getActiveGoals()
                val completedGoalsFlow = repository.getCompletedGoals()
                
                val activeGoals = mutableListOf<SavingsGoal>()
                activeGoalsFlow.collect { goalList ->
                    activeGoals.addAll(goalList)
                }
                
                val completedGoals = mutableListOf<SavingsGoal>()
                completedGoalsFlow.collect { goalList ->
                    completedGoals.addAll(goalList)
                }
                
                val totalTargetAmount = activeGoals.sumOf { it.targetAmount }
                
                _uiState.value = _uiState.value.copy(
                    activeGoals = activeGoals,
                    completedGoals = completedGoals,
                    totalTargetAmount = totalTargetAmount,
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

    fun addGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            try {
                repository.insertGoal(goal)
                loadGoals() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            try {
                repository.updateGoal(goal)
                loadGoals() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun deleteGoal(goal: SavingsGoal) {
        viewModelScope.launch {
            try {
                repository.deleteGoal(goal)
                loadGoals() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateGoalAmount(goalId: Long, amount: Double) {
        viewModelScope.launch {
            try {
                repository.updateGoalAmount(goalId, amount)
                loadGoals() // Reload data
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
