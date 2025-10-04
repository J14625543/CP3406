package com.personalfinance.tracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.personalfinance.tracker.R
import com.personalfinance.tracker.ui.screens.*

sealed class Screen(val route: String, val title: String, val shortTitle: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", "Home", Icons.Default.Dashboard)
    object Transactions : Screen("transactions", "Transactions", "Trans", Icons.Default.Receipt)
    object Budget : Screen("budget", "Budget", "Budget", Icons.Default.AccountBalanceWallet)
    object Goals : Screen("goals", "Goals", "Goals", Icons.Default.Savings)
    object Analytics : Screen("analytics", "Analytics", "Stats", Icons.Default.Analytics)
    object Settings : Screen("settings", "Settings", "Settings", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalFinanceNavigation() {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Dashboard,
        Screen.Transactions,
        Screen.Budget,
        Screen.Goals,
        Screen.Analytics,
        Screen.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                screen.icon, 
                                contentDescription = screen.title,
                                modifier = Modifier.size(20.dp)
                            ) 
                        },
                        label = { 
                            Text(
                                text = screen.shortTitle,
                                style = MaterialTheme.typography.labelSmall
                            ) 
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController = navController)
            }
            composable(Screen.Transactions.route) {
                TransactionsScreen(navController = navController)
            }
            composable(Screen.Budget.route) {
                BudgetScreen(navController = navController)
            }
            composable(Screen.Goals.route) {
                GoalsScreen(navController = navController)
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen(navController = navController)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }
        }
    }
}
