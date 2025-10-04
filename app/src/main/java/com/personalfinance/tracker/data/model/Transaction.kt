package com.personalfinance.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val category: String,
    val description: String,
    val date: Date,
    val createdAt: Date = Date()
)

enum class TransactionType {
    INCOME, EXPENSE
}

enum class Category(val displayName: String) {
    // Expense categories
    FOOD("Food"),
    TRANSPORT("Transport"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    UTILITIES("Utilities"),
    RENT("Rent"),
    
    // Income categories
    SALARY("Salary"),
    BONUS("Bonus"),
    INVESTMENT("Investment"),
    OTHER("Other");
    
    companion object {
        fun getExpenseCategories(): List<Category> {
            return listOf(FOOD, TRANSPORT, ENTERTAINMENT, SHOPPING, HEALTHCARE, EDUCATION, UTILITIES, RENT)
        }
        
        fun getIncomeCategories(): List<Category> {
            return listOf(SALARY, BONUS, INVESTMENT, OTHER)
        }
    }
}
