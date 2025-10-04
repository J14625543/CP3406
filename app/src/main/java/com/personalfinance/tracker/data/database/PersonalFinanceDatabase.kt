package com.personalfinance.tracker.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.personalfinance.tracker.data.converters.DateConverters
import com.personalfinance.tracker.data.converters.EnumConverters
import com.personalfinance.tracker.data.dao.*
import com.personalfinance.tracker.data.model.*

@Database(
    entities = [
        Transaction::class,
        Budget::class,
        SavingsGoal::class,
        BillReminder::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverters::class, EnumConverters::class)
abstract class PersonalFinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun billReminderDao(): BillReminderDao

    companion object {
        @Volatile
        private var INSTANCE: PersonalFinanceDatabase? = null

        fun getDatabase(context: Context): PersonalFinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PersonalFinanceDatabase::class.java,
                    "personal_finance_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
