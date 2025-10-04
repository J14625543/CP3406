package com.personalfinance.tracker.data.dao

import androidx.room.*
import com.personalfinance.tracker.data.model.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets WHERE currentMonth = :month AND currentYear = :year")
    fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>>

    @Query("SELECT * FROM budgets WHERE category = :category AND currentMonth = :month AND currentYear = :year")
    suspend fun getBudgetByCategory(category: String, month: Int, year: Int): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget): Long

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("DELETE FROM budgets WHERE id = :id")
    suspend fun deleteBudgetById(id: Long)

    @Query("SELECT * FROM budgets")
    fun getAllBudgets(): Flow<List<Budget>>
}
