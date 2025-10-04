package com.personalfinance.tracker.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.personalfinance.tracker.data.model.Transaction
import com.personalfinance.tracker.data.model.TransactionType
import com.personalfinance.tracker.ui.theme.ExpenseRed
import com.personalfinance.tracker.ui.theme.IncomeGreen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    currencyFormat: java.text.NumberFormat,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon
            Icon(
                imageVector = getCategoryIcon(transaction.category),
                contentDescription = transaction.category,
                modifier = Modifier.size(32.dp),
                tint = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Transaction information
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = transaction.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(transaction.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Amount
            Text(
                text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}${currencyFormat.format(transaction.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (transaction.type == TransactionType.INCOME) IncomeGreen else ExpenseRed
            )
        }
    }
}

@Composable
fun getCategoryIcon(category: String): ImageVector {
    return when (category) {
        "Food" -> Icons.Default.Restaurant
        "Transport" -> Icons.Default.DirectionsCar
        "Entertainment" -> Icons.Default.Movie
        "Shopping" -> Icons.Default.ShoppingCart
        "Healthcare" -> Icons.Default.LocalHospital
        "Education" -> Icons.Default.School
        "Utilities" -> Icons.Default.ElectricalServices
        "Rent" -> Icons.Default.Home
        "Salary" -> Icons.Default.Work
        "Bonus" -> Icons.Default.Celebration
        "Investment" -> Icons.Default.TrendingUp
        else -> Icons.Default.Category
    }
}
