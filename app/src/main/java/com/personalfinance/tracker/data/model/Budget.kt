package com.personalfinance.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val monthlyLimit: Double,
    val currentMonth: Int,
    val currentYear: Int,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

data class BudgetWithSpending(
    val budget: Budget,
    val spentAmount: Double,
    val remainingAmount: Double,
    val percentageUsed: Double
)
