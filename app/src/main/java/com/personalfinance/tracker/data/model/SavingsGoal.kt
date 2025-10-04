package com.personalfinance.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "savings_goals")
data class SavingsGoal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val targetDate: Date,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) {
    val progressPercentage: Double
        get() = if (targetAmount > 0) (currentAmount / targetAmount) * 100 else 0.0
    
    val remainingAmount: Double
        get() = targetAmount - currentAmount
    
    val isOnTrack: Boolean
        get() {
            val daysRemaining = (targetDate.time - Date().time) / (1000 * 60 * 60 * 24)
            val daysElapsed = (Date().time - createdAt.time) / (1000 * 60 * 60 * 24)
            val totalDays = (targetDate.time - createdAt.time) / (1000 * 60 * 60 * 24)
            
            if (totalDays <= 0) return currentAmount >= targetAmount
            
            val expectedProgress = daysElapsed.toDouble() / totalDays.toDouble()
            val actualProgress = currentAmount / targetAmount
            
            return actualProgress >= expectedProgress * 0.8 // Allow 20% deviation
        }
}
