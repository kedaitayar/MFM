package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.util.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `Transaction` ORDER BY transactionId DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

//    @Query("SELECT transactionId, transactionAmount, transactionTime, transactionType, accountName, budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId")
//    fun getAllTransaction2(): LiveData<List<TransactionWithAccountBudget>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction) : Int

    @Query("Delete from `transaction`")
    suspend fun deleteAll()

    @Update
    suspend fun update(transaction: Transaction)

    @Query("SELECT * FROM `transaction` WHERE :transactionId = transactionId")
    suspend fun getTransactionById(transactionId: Long): Transaction

    @Query("SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount, 0 AS transactionAccount, 0 AS transactionTime, 0 AS transactionType, 0 AS transactionAccountId, 0 AS transactionAccountTransferTo, 0 AS transactionId FROM `transaction` WHERE transactionTime BETWEEN :time AND :time2 GROUP BY transactionBudgetId")
    suspend fun getTransactionGrpByBudget(time: Calendar, time2: Calendar) : List<Transaction>

    @Query("SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount, 0 AS transactionAccount, 0 AS transactionTime, 0 AS transactionType, 0 AS transactionAccountId, 0 AS transactionAccountTransferTo, 0 AS transactionId FROM `transaction` WHERE transactionTime BETWEEN :time AND :time2 GROUP BY transactionBudgetId")
    fun getTransactionGrpByBudget2(time: Calendar, time2: Calendar) : List<Transaction>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionAccount,transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'INCOME' GROUP BY transactionAccountId")
    suspend fun getAccountIncome(): List<Transaction>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionAccount,transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'EXPENSE' GROUP BY transactionAccountId")
    suspend fun getAccountExpense(): List<Transaction>
}