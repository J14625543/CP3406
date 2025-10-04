package com.personalfinance.tracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "bill_reminders")
data class BillReminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val dueDate: Date,
    val category: String,
    val description: String? = null,
    val isPaid: Boolean = false,
    val isRecurring: Boolean = false,
    val recurringInterval: RecurringInterval? = null,
    val reminderDaysBefore: Int = 3,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
)

enum class RecurringInterval {
    WEEKLY, MONTHLY, QUARTERLY, YEARLY
}

data class BillReminderWithStatus(
    val billReminder: BillReminder,
    val isOverdue: Boolean,
    val daysUntilDue: Long
)
