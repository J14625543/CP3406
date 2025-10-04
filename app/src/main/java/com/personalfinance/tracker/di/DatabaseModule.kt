package com.personalfinance.tracker.di

import android.content.Context
import com.personalfinance.tracker.data.dao.*
import com.personalfinance.tracker.data.database.PersonalFinanceDatabase
import com.personalfinance.tracker.data.repository.PersonalFinanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PersonalFinanceDatabase {
        return PersonalFinanceDatabase.getDatabase(context)
    }

    @Provides
    fun provideTransactionDao(database: PersonalFinanceDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideBudgetDao(database: PersonalFinanceDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    fun provideSavingsGoalDao(database: PersonalFinanceDatabase): SavingsGoalDao {
        return database.savingsGoalDao()
    }

    @Provides
    fun provideBillReminderDao(database: PersonalFinanceDatabase): BillReminderDao {
        return database.billReminderDao()
    }

    @Provides
    @Singleton
    fun provideRepository(
        transactionDao: TransactionDao,
        budgetDao: BudgetDao,
        savingsGoalDao: SavingsGoalDao,
        billReminderDao: BillReminderDao
    ): PersonalFinanceRepository {
        return PersonalFinanceRepository(
            transactionDao = transactionDao,
            budgetDao = budgetDao,
            savingsGoalDao = savingsGoalDao,
            billReminderDao = billReminderDao
        )
    }
}
