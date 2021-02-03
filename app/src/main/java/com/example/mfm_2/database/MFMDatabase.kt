package com.example.mfm_2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mfm_2.dao.*
import com.example.mfm_2.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@Database(entities = [Account::class, Transaction::class, Budget::class, BudgetTransaction::class, BudgetType::class, BudgetDeadline::class], version = 26, exportSchema = true)
@TypeConverters(Converters::class)
abstract class MFMDatabase : RoomDatabase(){
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun budgetTypeDao(): BudgetTypeDao
    abstract fun budgetTransactionDao(): BudgetTransactionDao
    abstract fun budgetDeadlineDao(): BudgetDeadlineDao

    companion object{
        @Volatile
        private var INSTANCE: MFMDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): MFMDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        MFMDatabase::class.java,
                        "mfm_database"
                    )
//                    .addCallback(DatabaseCallback(scope))
//                    .fallbackToDestructiveMigration()
                    .createFromAsset("databases/db 01.db")
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onOpen method to populate the database.
             * For this sample, we clear the database every time it is created or opened.
             */
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let {
                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(it.accountDao())
//                        populateDatabase(it.budgetDao())
//                        populateDatabase(it.budgetTypeDao())
//                        populateDatabase(it.transactionDao())
//                        populateDatabase(it.budgetDeadlineDao())
//                        populateDatabase(it.budgetTransactionDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(accountDao: AccountDao){
            accountDao.deleteAll()

            var account = Account(accountName = "Cash", accountId = 1)
            accountDao.insert(account)
            account = Account(accountName = "Bank", accountId = 2)
            accountDao.insert(account)
            account = Account(accountName = "Bank 2", accountId = 3)
            accountDao.insert(account)
        }

        suspend fun populateDatabase(transactionDao: TransactionDao){
            transactionDao.deleteAll()
            val cal = Calendar.getInstance()
            cal.add(Calendar.MONTH,-1)
            var transaction = Transaction(transactionAmount = 200.0, transactionType = "INCOME", transactionAccountId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 500.0, transactionType = "INCOME", transactionAccountId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "TRANSFER", transactionAccountId = 2, transactionAccountTransferTo = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 200.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 3, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 90.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 4, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 2)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 2)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)
            transaction = Transaction(transactionAmount = 500.0, transactionType = "INCOME", transactionAccountId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 2)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 2)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)
            transaction = Transaction(transactionAmount = 300.0, transactionType = "TRANSFER", transactionAccountId = 2, transactionAccountTransferTo = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 2)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            transaction = Transaction(transactionAmount = 200.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 3, transactionTime = cal)
            transactionDao.insert(transaction)

            transaction = Transaction(transactionAmount = 100.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 4, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 2, transactionBudgetId = 2, transactionTime = cal)
            transactionDao.insert(transaction)

            cal.add(Calendar.DATE, 1)
            transaction = Transaction(transactionAmount = 10.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1, transactionTime = cal)
            transactionDao.insert(transaction)
        }

        suspend fun populateDatabase(budgetDao: BudgetDao){
            budgetDao.deleteAll()

            var budget = Budget(budgetName = "Food", budgetId = 1, budgetType = 1)
            budgetDao.insert(budget)
            budget = Budget(budgetName = "Transport", budgetId = 2, budgetType = 1)
            budgetDao.insert(budget)
            budget = Budget(budgetName = "Rent", budgetId = 3, budgetType = 1)
            budgetDao.insert(budget)
            budget = Budget(budgetName = "Utilities", budgetId = 4, budgetType = 1)
            budgetDao.insert(budget)
            budget = Budget(budgetName = "Insurance", budgetId = 5, budgetType = 2)
            budgetDao.insert(budget)
            budget = Budget(budgetName = "Vehicle Maintenance", budgetId = 6, budgetType = 2)
            budgetDao.insert(budget)
//            budget = Budget(budgetName = "Vacation", budgetId = 5, budgetType = 3)
//            budgetDao.insert(budget)
        }

        suspend fun populateDatabase(budgetTypeDao: BudgetTypeDao){
            budgetTypeDao.deleteAll()

            var budgetType = BudgetType(budgetTypeId = 1, budgetTypeName = "Monthly")
            budgetTypeDao.insert(budgetType)
            budgetType = BudgetType(budgetTypeId = 2, budgetTypeName = "Yearly")
            budgetTypeDao.insert(budgetType)
            budgetType = BudgetType(budgetTypeId = 3, budgetTypeName = "Goal/Debt")
            budgetTypeDao.insert(budgetType)
        }

        suspend fun populateDatabase(budgetTransactionDao: BudgetTransactionDao){
//            budgetTransactionDao.deleteAll()

            var budgetTransaction = BudgetTransaction(budgetTransactionMonth = 5, budgetTransactionYear = 2020, budgetTransactionAmount = 10.0, budgetTransactionBudgetId = 1)
            budgetTransactionDao.insert(budgetTransaction)
            budgetTransaction = BudgetTransaction(budgetTransactionMonth = 5, budgetTransactionYear = 2020, budgetTransactionAmount = 20.0, budgetTransactionBudgetId = 2)
            budgetTransactionDao.insert(budgetTransaction)
            budgetTransaction = BudgetTransaction(budgetTransactionMonth = 6, budgetTransactionYear = 2020, budgetTransactionAmount = 11.0, budgetTransactionBudgetId = 1)
            budgetTransactionDao.insert(budgetTransaction)
            budgetTransaction = BudgetTransaction(budgetTransactionMonth = 7, budgetTransactionYear = 2020, budgetTransactionAmount = 21.0, budgetTransactionBudgetId = 1)
            budgetTransactionDao.insert(budgetTransaction)
        }

        suspend fun populateDatabase(budgetDeadlineDao: BudgetDeadlineDao){
            budgetDeadlineDao.deleteAll()

            val cal = Calendar.getInstance()
            cal.set(2020,11,1)
            var budgetDeadline = BudgetDeadline(budgetId = 4, budgetDeadline = cal)
            budgetDeadlineDao.insert(budgetDeadline)
            budgetDeadline = BudgetDeadline(budgetId = 5, budgetDeadline = cal)
            budgetDeadlineDao.insert(budgetDeadline)
        }
    }
}