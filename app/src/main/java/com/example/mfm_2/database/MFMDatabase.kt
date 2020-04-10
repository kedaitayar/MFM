package com.example.mfm_2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mfm_2.dao.AccountDao
import com.example.mfm_2.dao.BudgetDao
import com.example.mfm_2.dao.DebtDao
import com.example.mfm_2.dao.TransactionDao
import com.example.mfm_2.model.Account
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.Debt
import com.example.mfm_2.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Account::class, Transaction::class, Budget::class, Debt::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class MFMDatabase : RoomDatabase(){
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun debtDao(): DebtDao

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
                    .addCallback(DatabaseCallback(scope)).fallbackToDestructiveMigration()
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
//                        populateDatabase(it.transactionDao())
//                        populateDatabase(it.debtDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(accountDao: AccountDao){
            accountDao.deleteAll()

            var account = Account(accountName = "Cash", accountBalance = 100.0, accountAllocationBalance = 100.0, accountId = 1)
            accountDao.insert(account)
            account = Account(accountName = "Bank", accountBalance = 50.0, accountAllocationBalance = 50.0, accountId = 2)
            accountDao.insert(account)
        }

        suspend fun populateDatabase(transactionDao: TransactionDao){
            transactionDao.deleteAll()

            var transaction = Transaction(transactionAmount = 15.0, transactionType = "EXPENSE", transactionAccountId = 1, transactionBudgetId = 1)
            transactionDao.insert(transaction)
//            transaction = Transaction(transactionAmount = 20, transactionType = "EXPENSE", transactionAccount = "Cash", transactionBudget = "Food")
//            transactionDao.insert(transaction)
//            transaction = Transaction(transactionAmount = 25, transactionType = "INCOME", transactionAccount = "Bank", transactionBudget = "Transport")
//            transactionDao.insert(transaction)
//            transaction = Transaction(transactionAmount = 30, transactionType = "INCOME", transactionAccount = "Bank", transactionBudget = "Food")
//            transactionDao.insert(transaction)
//            transaction = Transaction(transactionAmount = 35, transactionType = "EXPENSE", transactionAccount = "Cash", transactionBudget = "Transport")
//            transactionDao.insert(transaction)

        }

        suspend fun populateDatabase(budgetDao: BudgetDao){
            budgetDao.deleteAll()

            var budget = Budget(budgetName = "Food", budgetAllocation = 50.0, budgetId = 1)
            budgetDao.insert(budget)
            budget = Budget(budgetName = "Transport", budgetAllocation = 50.0, budgetId = 2)
            budgetDao.insert(budget)
        }

        suspend fun populateDatabase(debtDao: DebtDao){
            debtDao.deleteAll()

            var debt = Debt(debtName = "Ahlong")
            debtDao.insert(debt)
        }
    }
}