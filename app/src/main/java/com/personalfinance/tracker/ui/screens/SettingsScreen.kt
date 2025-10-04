package com.personalfinance.tracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.personalfinance.tracker.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top title
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Notification settings
            item {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = "Notification Settings",
                    subtitle = "Manage bill reminders and budget notifications",
                    onClick = { /* TODO: Navigate to notification settings page */ }
                )
            }

            // Currency settings
            item {
                SettingsItem(
                    icon = Icons.Default.AttachMoney,
                    title = "Currency Settings",
                    subtitle = "Set default currency",
                    onClick = { /* TODO: Navigate to currency settings page */ }
                )
            }

            // Data management
            item {
                SettingsItem(
                    icon = Icons.Default.Storage,
                    title = "Data Management",
                    subtitle = "Backup and restore data",
                    onClick = { /* TODO: Navigate to data management page */ }
                )
            }

            // About app
            item {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "About App",
                    subtitle = "Version information and help",
                    onClick = { /* TODO: Navigate to about page */ }
                )
            }

            // Privacy policy
            item {
                SettingsItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Privacy Policy",
                    subtitle = "View privacy policy",
                    onClick = { /* TODO: Navigate to privacy policy page */ }
                )
            }

            // Logout
            item {
                SettingsItem(
                    icon = Icons.Default.ExitToApp,
                    title = "Logout",
                    subtitle = "Exit application",
                    onClick = { /* TODO: Implement logout functionality */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
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
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Enter",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
