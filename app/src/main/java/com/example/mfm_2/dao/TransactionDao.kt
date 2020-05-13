package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.AccountListAdapterDataObject
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.util.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `Transaction` ORDER BY transactionId DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

//    @Query("SELECT transactionId, transactionAmount, transactionTime, transactionType, accountName, budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId")
//    fun getAllTransaction2(): LiveData<List<TransactionWithAccountBudget>>

    @Insert
    suspend fun insert(transaction: Transaction) : Long

    @Delete
    suspend fun delete(transaction: Transaction) : Int

    @Query("Delete from `transaction`")
    suspend fun deleteAll()

    @Update
    suspend fun update(transaction: Transaction) : Int

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

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionAccount,transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountId, transactionAccountTransferTo")
    suspend fun getAccountTransfer(): List<Transaction>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionAccount,transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'INCOME' GROUP BY transactionAccountId")
    fun getAccountIncomeLV(): LiveData<List<Transaction>>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionAccount,transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'EXPENSE' GROUP BY transactionAccountId")
    fun getAccountExpenseLV(): LiveData<List<Transaction>>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionAccount,transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountId, transactionAccountTransferTo")
    fun getAccountTransferLV(): LiveData<List<Transaction>>

//    @Query("SELECT tbl.accountId, accountName, SUM(accountIncome) AS accountIncome, SUM(accountExpense) AS accountExpense, SUM(accountTransferIn) AS accountTransferIn, SUM(accountTransferOut) AS accountTransferOut FROM (SELECT transactionAccountId AS accountId, SUM(CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END) AS accountIncome, SUM(CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END) AS accountExpense, 0 AS accountTransferIn, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferOut FROM `transaction` GROUP BY transactionAccountId UNION SELECT transactionAccountTransferTo AS accountId, 0 AS accountIncome, 0 AS accountExpense, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferIn, 0 AS accountTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountTransferTo) AS tbl INNER JOIN account ON tbl.accountId = account.accountId GROUP BY tbl.accountId")
//    fun getAccountListData(): LiveData<List<AccountListAdapterDataObject>>

//    @Query("SELECT accountId, accountName, SUM(accountIncome) AS accountIncome, SUM(accountExpense) AS accountExpense, SUM(accountTransferIn) AS accountTransferIn, SUM(accountTransferOut) AS accountTransferOut FROM (SELECT * FROM account LEFT JOIN (SELECT transactionAccountId AS accountId, SUM(CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END) AS accountIncome, SUM(CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END) AS accountExpense, 0 AS accountTransferIn, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferOut FROM `transaction` GROUP BY transactionAccountId UNION SELECT transactionAccountTransferTo AS accountId, 0 AS accountIncome, 0 AS accountExpense, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferIn, 0 AS accountTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountTransferTo) AS tbl ON tbl.accountId = account.accountId GROUP BY tbl.accountId)")
//    fun getAccountListData(): LiveData<List<AccountListAdapterDataObject>>


    @Query("SELECT account.accountId, accountName, SUM(accountIncome) AS accountIncome, SUM(accountExpense) AS accountExpense, SUM(accountTransferIn) AS accountTransferIn, SUM(accountTransferOut) AS accountTransferOut FROM account LEFT JOIN (SELECT transactionAccountId AS accountId, SUM(CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END) AS accountIncome, SUM(CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END) AS accountExpense, 0 AS accountTransferIn, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferOut FROM `transaction` GROUP BY transactionAccountId UNION SELECT transactionAccountTransferTo AS accountId, 0 AS accountIncome, 0 AS accountExpense, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferIn, 0 AS accountTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountTransferTo) AS tbl ON account.accountId = tbl.accountId GROUP BY account.accountId")
    fun getAccountListData(): LiveData<List<AccountListAdapterDataObject>>

//    @Query("SELECT transactionAccountId AS accountId, 0 AS accountName, SUM(CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END) AS accountIncome, SUM(CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END) AS accountExpense, 0 AS accountTransferIn, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferOut FROM `transaction` GROUP BY transactionAccountId UNION SELECT transactionAccountTransferTo AS accountId, 0 AS accountName, 0 AS accountIncome, 0 AS accountExpense, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferIn, 0 AS accountTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountTransferTo")
//    fun getAccountListData(): LiveData<List<AccountListAdapterDataObject>>
}