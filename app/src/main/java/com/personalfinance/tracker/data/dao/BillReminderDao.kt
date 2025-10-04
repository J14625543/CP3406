package com.personalfinance.tracker.data.dao

import androidx.room.*
import com.personalfinance.tracker.data.model.BillReminder
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BillReminderDao {
    @Query("SELECT * FROM bill_reminders ORDER BY dueDate ASC")
    fun getAllBillReminders(): Flow<List<BillReminder>>

    @Query("SELECT * FROM bill_reminders WHERE isPaid = 0 ORDER BY dueDate ASC")
    fun getUnpaidBills(): Flow<List<BillReminder>>

    @Query("SELECT * FROM bill_reminders WHERE dueDate BETWEEN :startDate AND :endDate ORDER BY dueDate ASC")
    fun getBillsByDateRange(startDate: Date, endDate: Date): Flow<List<BillReminder>>

    @Query("SELECT * FROM bill_reminders WHERE dueDate <= :date AND isPaid = 0")
    fun getOverdueBills(date: Date): Flow<List<BillReminder>>

    @Query("SELECT * FROM bill_reminders WHERE id = :id")
    suspend fun getBillById(id: Long): BillReminder?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillReminder): Long

    @Update
    suspend fun updateBill(bill: BillReminder)

    @Delete
    suspend fun deleteBill(bill: BillReminder)

    @Query("DELETE FROM bill_reminders WHERE id = :id")
    suspend fun deleteBillById(id: Long)

    @Query("UPDATE bill_reminders SET isPaid = :isPaid, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateBillPaymentStatus(id: Long, isPaid: Boolean, updatedAt: Date)
}
