package com.personalfinance.tracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personalfinance.tracker.data.model.BillReminderWithStatus
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BillReminderItem(
    bill: BillReminderWithStatus,
    currencyFormat: java.text.NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (bill.isOverdue) 
                MaterialTheme.colorScheme.errorContainer 
            else 
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bill icon
            Icon(
                imageVector = getBillIcon(bill.billReminder.category),
                contentDescription = bill.billReminder.category,
                modifier = Modifier.size(32.dp),
                tint = if (bill.isOverdue) 
                    MaterialTheme.colorScheme.error 
                else 
                    MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Bill information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = bill.billReminder.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = bill.billReminder.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Due Date: ${SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(bill.billReminder.dueDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Amount and status
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = currencyFormat.format(bill.billReminder.amount),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (bill.isOverdue) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.onSurface
                )
                
                if (bill.isOverdue) {
                    Text(
                        text = "Overdue",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else if (bill.daysUntilDue <= 3) {
                    Text(
                        text = "Due in ${bill.daysUntilDue} days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                } else {
                    Text(
                        text = "Due in ${bill.daysUntilDue} days",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun getBillIcon(category: String): ImageVector {
    return when (category) {
        "Utilities" -> Icons.Default.ElectricalServices
        "Rent" -> Icons.Default.Home
        "Healthcare" -> Icons.Default.LocalHospital
        "Insurance" -> Icons.Default.Security
        "Credit Card" -> Icons.Default.CreditCard
        "Loan" -> Icons.Default.AccountBalance
        "Internet" -> Icons.Default.Wifi
        "Phone" -> Icons.Default.Phone
        else -> Icons.Default.Receipt
    }
}
