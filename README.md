# Wealth Secret

A comprehensive Android personal finance management application that helps users track income and expenses, manage budgets, set savings goals, and provides intelligent financial analysis and recommendations.

## Features

### 📊 Dashboard
- Total balance display
- Monthly income and expense overview
- Savings rate calculation
- Recent transaction records
- Budget status monitoring
- Upcoming bill reminders

### 💰 Transaction Management
- Record income and expenses
- Category management (dining, transportation, entertainment, shopping, etc.)
- Transaction history viewing
- Transaction editing and deletion
- Filter by type and date

### 📈 Budget Management
- Set monthly budget limits
- Real-time budget usage tracking
- Budget overspend alerts
- Category-based budget management
- Budget progress visualization

### 🎯 Savings Goals
- Create savings goals
- Goal progress tracking
- Goal completion status
- Goal timeline management
- Progress lag alerts

### 📊 Data Analytics
- Expense trend analysis
- Category expense pie charts
- Savings trend charts
- Intelligent spending recommendations
- Budget optimization suggestions

### 🔔 Bill Reminders
- Bill due date reminders
- Recurring bill settings
- Overdue bill marking
- Custom reminder timing

## Technical Architecture

### Development Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture Pattern**: MVVM + Repository
- **Dependency Injection**: Hilt
- **Database**: Room
- **Asynchronous Processing**: Kotlin Coroutines + Flow
- **Navigation**: Navigation Compose

### Project Structure
```
app/src/main/java/com/personalfinance/tracker/
├── data/
│   ├── converters/          # Database type converters
│   ├── dao/                 # Data Access Objects
│   ├── database/            # Database configuration
│   ├── model/               # Data models
│   └── repository/          # Data repository
├── di/                      # Dependency injection modules
├── ui/
│   ├── components/          # UI components
│   ├── navigation/          # Navigation configuration
│   ├── screens/             # Screen interfaces
│   ├── theme/               # Theme configuration
│   └── viewmodel/           # View models
├── MainActivity.kt          # Main activity
└── PersonalFinanceApplication.kt  # Application class
```

## Installation and Setup

### Requirements
- Android Studio Arctic Fox or higher
- Android SDK 24 or higher
- Kotlin 1.9.10 or higher

### Build Steps
1. Clone the project to your local machine
2. Open the project with Android Studio
3. Wait for Gradle sync to complete
4. Connect an Android device or start an emulator
5. Click the run button to build and install the app

### Dependencies
- Jetpack Compose BOM
- Room Database
- Hilt Dependency Injection
- Navigation Compose
- Material3 Design
- Kotlin Coroutines
- Work Manager

## Usage Guide

### First Time Setup
1. After launching the app, the system will automatically create the database
2. View your current financial status on the dashboard page
3. Click the "+" button in the bottom right corner to start adding transaction records

### Adding Transactions
1. Navigate to the "Transactions" page
2. Click the "+" button
3. Select transaction type (income/expense)
4. Enter amount, select category, add description
5. Choose date and save

### Setting Budgets
1. Navigate to the "Budget" page
2. Click the "+" button
3. Select category and set monthly limit
4. Save and view budget status on the dashboard

### Creating Savings Goals
1. Navigate to the "Savings Goals" page
2. Click the "+" button
3. Enter goal name, target amount, target date
4. Add description and save
5. Regularly update current amount to track progress

### Viewing Analytics
1. Navigate to the "Analytics" page
2. View expense trends and category breakdowns
3. Read intelligent recommendations
4. Adjust spending habits based on suggestions

## Data Security

- All data is stored in local SQLite database
- Supports data backup and recovery
- Follows Android data extraction rules
- Does not collect user privacy information

## Future Plans

- [ ] Data export functionality (CSV/Excel)
- [ ] Multi-account management
- [ ] Investment portfolio tracking
- [ ] Debt management
- [ ] Financial report generation
- [ ] Cloud sync functionality
- [ ] Multi-language support
- [ ] Dark theme optimization

## Contributing

Issues and Pull Requests are welcome to improve this project.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For questions or suggestions, please contact through:
- Submit GitHub Issues
- Send email to the developer

---

**Note**: This is an educational project for learning and reference purposes only. Please ensure thorough testing of all features before actual use.