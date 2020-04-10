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
//    @Query("SELECT * FROM `Transaction` ORDER BY transactionId DESC")
//    fun getAllTransaction(): LiveData<List<Transaction>>

    @Query("SELECT transactionId, transactionAmount, transactionTime, transactionType, accountName, budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId")
    fun getAllTransaction2(): LiveData<List<TransactionWithAccountBudget>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("Delete from `transaction`")
    suspend fun deleteAll()

    @Update
    suspend fun update(transaction: Transaction)

//    @androidx.room.Transaction
//    @Query("SELECT * FROM Account")
//    suspend fun getTransactionWithAccount(): List<TransactionWithAccountAndBudget>
//
//    @Query("SELECT * FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId")
//    suspend fun getTransactionWithAccount2(): List<TransactionWithAccountAndBudget>
//
    //    @androidx.room.Transaction
//    @Query("SELECT transactionId, transactionAmount, transactionTime, accountName, budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId")
//    suspend fun getTransactionWithAccountBudget(): List<TransactionWithAccountBudget>

//    @Query("SELECT `Transaction`.transactionId, `Transaction`.transactionAmount, Account.accountName, Budget.budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId")
//    suspend fun getTransctionFull(): List<TransactionTest>

}