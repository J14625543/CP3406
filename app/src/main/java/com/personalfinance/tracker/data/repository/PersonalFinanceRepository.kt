package com.personalfinance.tracker.data.repository

import com.personalfinance.tracker.data.dao.*
import com.personalfinance.tracker.data.model.*
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalFinanceRepository @Inject constructor(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val savingsGoalDao: SavingsGoalDao,
    private val billReminderDao: BillReminderDao
) {
    
    // Transaction methods
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()
    
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByType(type)
    
    fun getTransactionsByDateRange(startDate: Date, endDate: Date): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByDateRange(startDate, endDate)
    
    fun getTransactionsByCategory(category: String): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByCategory(category)
    
    fun getTransactionsByDateRangeAndType(startDate: Date, endDate: Date, type: TransactionType): Flow<List<Transaction>> = 
        transactionDao.getTransactionsByDateRangeAndType(startDate, endDate, type)
    
    suspend fun getTotalAmountByTypeAndDateRange(type: TransactionType, startDate: Date, endDate: Date): Double? = 
        transactionDao.getTotalAmountByTypeAndDateRange(type, startDate, endDate)
    
    suspend fun getTotalAmountByCategoryAndDateRange(category: String, startDate: Date, endDate: Date): Double? = 
        transactionDao.getTotalAmountByCategoryAndDateRange(category, startDate, endDate)
    
    fun getRecentTransactions(limit: Int): Flow<List<Transaction>> = 
        transactionDao.getRecentTransactions(limit)
    
    suspend fun insertTransaction(transaction: Transaction): Long = 
        transactionDao.insertTransaction(transaction)
    
    suspend fun updateTransaction(transaction: Transaction) = 
        transactionDao.updateTransaction(transaction)
    
    suspend fun deleteTransaction(transaction: Transaction) = 
        transactionDao.deleteTransaction(transaction)
    
    suspend fun deleteTransactionById(id: Long) = 
        transactionDao.deleteTransactionById(id)
    
    // Budget methods
    fun getBudgetsForMonth(month: Int, year: Int): Flow<List<Budget>> = 
        budgetDao.getBudgetsForMonth(month, year)
    
    suspend fun getBudgetByCategory(category: String, month: Int, year: Int): Budget? = 
        budgetDao.getBudgetByCategory(category, month, year)
    
    suspend fun insertBudget(budget: Budget): Long = 
        budgetDao.insertBudget(budget)
    
    suspend fun updateBudget(budget: Budget) = 
        budgetDao.updateBudget(budget)
    
    suspend fun deleteBudget(budget: Budget) = 
        budgetDao.deleteBudget(budget)
    
    suspend fun deleteBudgetById(id: Long) = 
        budgetDao.deleteBudgetById(id)
    
    fun getAllBudgets(): Flow<List<Budget>> = 
        budgetDao.getAllBudgets()
    
    // Savings Goal methods
    fun getAllGoals(): Flow<List<SavingsGoal>> = 
        savingsGoalDao.getAllGoals()
    
    fun getActiveGoals(): Flow<List<SavingsGoal>> = 
        savingsGoalDao.getActiveGoals()
    
    fun getCompletedGoals(): Flow<List<SavingsGoal>> = 
        savingsGoalDao.getCompletedGoals()
    
    suspend fun getGoalById(id: Long): SavingsGoal? = 
        savingsGoalDao.getGoalById(id)
    
    suspend fun insertGoal(goal: SavingsGoal): Long = 
        savingsGoalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: SavingsGoal) = 
        savingsGoalDao.updateGoal(goal)
    
    suspend fun deleteGoal(goal: SavingsGoal) = 
        savingsGoalDao.deleteGoal(goal)
    
    suspend fun deleteGoalById(id: Long) = 
        savingsGoalDao.deleteGoalById(id)
    
    suspend fun updateGoalAmount(id: Long, amount: Double, updatedAt: Date = Date()) = 
        savingsGoalDao.updateGoalAmount(id, amount, updatedAt)
    
    // Bill Reminder methods
    fun getAllBillReminders(): Flow<List<BillReminder>> = 
        billReminderDao.getAllBillReminders()
    
    fun getUnpaidBills(): Flow<List<BillReminder>> = 
        billReminderDao.getUnpaidBills()
    
    fun getBillsByDateRange(startDate: Date, endDate: Date): Flow<List<BillReminder>> = 
        billReminderDao.getBillsByDateRange(startDate, endDate)
    
    fun getOverdueBills(date: Date): Flow<List<BillReminder>> = 
        billReminderDao.getOverdueBills(date)
    
    suspend fun getBillById(id: Long): BillReminder? = 
        billReminderDao.getBillById(id)
    
    suspend fun insertBill(bill: BillReminder): Long = 
        billReminderDao.insertBill(bill)
    
    suspend fun updateBill(bill: BillReminder) = 
        billReminderDao.updateBill(bill)
    
    suspend fun deleteBill(bill: BillReminder) = 
        billReminderDao.deleteBill(bill)
    
    suspend fun deleteBillById(id: Long) = 
        billReminderDao.deleteBillById(id)
    
    suspend fun updateBillPaymentStatus(id: Long, isPaid: Boolean, updatedAt: Date = Date()) = 
        billReminderDao.updateBillPaymentStatus(id, isPaid, updatedAt)
    
    // Helper methods for dashboard
    suspend fun getUpcomingBills(): List<BillReminderWithStatus> {
        val calendar = Calendar.getInstance()
        val today = calendar.time
        
        // Get bills for next 30 days
        calendar.add(Calendar.DAY_OF_MONTH, 30)
        val futureDate = calendar.time
        
        val bills = billReminderDao.getBillsByDateRange(today, futureDate)
        val billsList = mutableListOf<BillReminder>()
        
        bills.collect { billList ->
            billsList.addAll(billList)
        }
        
        return billsList.map { bill ->
            val daysUntilDue = (bill.dueDate.time - today.time) / (1000 * 60 * 60 * 24)
            val isOverdue = bill.dueDate.before(today) && !bill.isPaid
            
            BillReminderWithStatus(bill, isOverdue, daysUntilDue)
        }.sortedBy { it.billReminder.dueDate }
    }
}
