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

data class Recommendation(
    val title: String,
    val description: String,
    val type: RecommendationType,
    val priority: RecommendationPriority
)

enum class RecommendationType {
    SAVINGS, BUDGET, SPENDING, GOAL
}

enum class RecommendationPriority {
    HIGH, MEDIUM, LOW
}

@Composable
fun RecommendationItem(
    recommendation: Recommendation,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (recommendation.priority) {
                RecommendationPriority.HIGH -> MaterialTheme.colorScheme.errorContainer
                RecommendationPriority.MEDIUM -> MaterialTheme.colorScheme.primaryContainer
                RecommendationPriority.LOW -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getRecommendationIcon(recommendation.type),
                contentDescription = recommendation.title,
                modifier = Modifier.size(24.dp),
                tint = when (recommendation.priority) {
                    RecommendationPriority.HIGH -> MaterialTheme.colorScheme.error
                    RecommendationPriority.MEDIUM -> MaterialTheme.colorScheme.primary
                    RecommendationPriority.LOW -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = recommendation.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Priority indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = "Priority",
                    modifier = Modifier.size(8.dp),
                    tint = when (recommendation.priority) {
                        RecommendationPriority.HIGH -> MaterialTheme.colorScheme.error
                        RecommendationPriority.MEDIUM -> MaterialTheme.colorScheme.primary
                        RecommendationPriority.LOW -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
fun getRecommendationIcon(type: RecommendationType): ImageVector {
    return when (type) {
        RecommendationType.SAVINGS -> Icons.Default.Savings
        RecommendationType.BUDGET -> Icons.Default.AccountBalanceWallet
        RecommendationType.SPENDING -> Icons.Default.ShoppingCart
        RecommendationType.GOAL -> Icons.Default.Flag
    }
}
