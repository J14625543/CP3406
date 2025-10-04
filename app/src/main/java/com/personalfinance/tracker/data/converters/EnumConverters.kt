package com.personalfinance.tracker.data.converters

import androidx.room.TypeConverter
import com.personalfinance.tracker.data.model.RecurringInterval
import com.personalfinance.tracker.data.model.TransactionType

class EnumConverters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }

    @TypeConverter
    fun toTransactionType(type: String): TransactionType {
        return TransactionType.valueOf(type)
    }

    @TypeConverter
    fun fromRecurringInterval(interval: RecurringInterval?): String? {
        return interval?.name
    }

    @TypeConverter
    fun toRecurringInterval(interval: String?): RecurringInterval? {
        return interval?.let { RecurringInterval.valueOf(it) }
    }
}
