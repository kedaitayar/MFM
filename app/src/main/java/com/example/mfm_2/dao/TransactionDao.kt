package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `Transaction` ORDER BY transactionId DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

    @Query("SELECT transactionId, transactionAmount, transactionTime, transactionType, accountName, budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId")
    fun getAllTransaction2(): LiveData<List<TransactionWithAccountBudget>>

//    @Query("SELECT transactionId, transactionAmount, transactionTime, transactionType, accountName, budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId WHERE ")
//    fun getAccountTransaction(account: String): LiveData<List<TransactionWithAccountBudget>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("Delete from `transaction`")
    suspend fun deleteAll()

    @Update
    suspend fun update(transaction: Transaction)

//    @Query("SELECT transactionBudgetId, SUM(transactionAmount) FROM `transaction` WHERE transactionTime BETWEEN  GROUP BY transactionBudgetId")
//    suspend fun getTransactionGrpByBudget(time: String, time2: String) : List<Transaction>
}