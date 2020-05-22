package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.AccountListAdapterDataObject
import com.example.mfm_2.pojo.AccountTransactionChartDataObject
import com.example.mfm_2.pojo.TransactionListAdapterDataObject
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.util.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `Transaction` ORDER BY transactionId DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

//    @Query("SELECT transactionId, transactionAmount, transactionTime, transactionType, accountName, budgetName FROM `Transaction` INNER JOIN Account ON `Transaction`.transactionAccountId = Account.accountId INNER JOIN Budget ON `Transaction`.transactionBudgetId = Budget.budgetId")
//    fun getAllTransaction2(): LiveData<List<TransactionWithAccountBudget>>

    @Insert
    suspend fun insert(transaction: Transaction): Long

    @Delete
    suspend fun delete(transaction: Transaction): Int

    @Query("Delete from `transaction`")
    suspend fun deleteAll()

    @Update
    suspend fun update(transaction: Transaction): Int

    @Query("SELECT * FROM `transaction` WHERE :transactionId = transactionId")
    suspend fun getTransactionById(transactionId: Long): Transaction

    @Query("SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount, 0 AS transactionAccount, 0 AS transactionTime, 0 AS transactionType, 0 AS transactionAccountId, 0 AS transactionAccountTransferTo, 0 AS transactionId FROM `transaction` WHERE transactionTime BETWEEN :time AND :time2 GROUP BY transactionBudgetId")
    suspend fun getTransactionGrpByBudget(time: Calendar, time2: Calendar): List<Transaction>

    @Query("SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount, 0 AS transactionAccount, 0 AS transactionTime, 0 AS transactionType, 0 AS transactionAccountId, 0 AS transactionAccountTransferTo, 0 AS transactionId FROM `transaction` WHERE transactionTime BETWEEN :time AND :time2 GROUP BY transactionBudgetId")
    fun getTransactionGrpByBudget2(time: Calendar, time2: Calendar): List<Transaction>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'INCOME' GROUP BY transactionAccountId")
    suspend fun getAccountIncome(): List<Transaction>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'EXPENSE' GROUP BY transactionAccountId")
    suspend fun getAccountExpense(): List<Transaction>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountId, transactionAccountTransferTo")
    suspend fun getAccountTransfer(): List<Transaction>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'INCOME' GROUP BY transactionAccountId")
    fun getAccountIncomeLV(): LiveData<List<Transaction>>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount, transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'EXPENSE' GROUP BY transactionAccountId")
    fun getAccountExpenseLV(): LiveData<List<Transaction>>

    @Query("SELECT transactionAccountId, SUM(transactionAmount) as transactionAmount ,transactionTime,transactionType,transactionAccountTransferTo,transactionId FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountId, transactionAccountTransferTo")
    fun getAccountTransferLV(): LiveData<List<Transaction>>

    @Query("SELECT account.accountId, accountName, SUM(accountIncome) AS accountIncome, SUM(accountExpense) AS accountExpense, SUM(accountTransferIn) AS accountTransferIn, SUM(accountTransferOut) AS accountTransferOut FROM account LEFT JOIN (SELECT transactionAccountId AS accountId, SUM(CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END) AS accountIncome, SUM(CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END) AS accountExpense, 0 AS accountTransferIn, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferOut FROM `transaction` GROUP BY transactionAccountId UNION SELECT transactionAccountTransferTo AS accountId, 0 AS accountIncome, 0 AS accountExpense, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferIn, 0 AS accountTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' GROUP BY transactionAccountTransferTo) AS tbl ON account.accountId = tbl.accountId GROUP BY account.accountId")
    fun getAccountListData(): LiveData<List<AccountListAdapterDataObject>>

    @Query("SELECT *, accountName AS transactionAccountName, budget.budgetName AS transactionBudgetName, budget2.budgetName AS transactionAccountTransferToName  FROM `transaction` LEFT JOIN account ON transactionAccountId = accountId LEFT JOIN budget ON transactionBudgetId = budget.budgetId LEFT JOIN budget AS budget2 ON transactionAccountTransferTo = budget2.budgetId ORDER BY transactionId DESC")
    fun getTransactionListData(): LiveData<List<TransactionListAdapterDataObject>>

    @Query("SELECT SUM(transactionAmount) FROM `transaction` GROUP BY date(transactionTime), transactionType")
    suspend fun getTime(): List<String>

//    @Query("SELECT date(transactionTime) AS accountTransactionDate, SUM(transactionAmount) AS accountTransactionAmount FROM `transaction` WHERE transactionType = 'EXPENSE' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId = :accountId GROUP BY date(transactionTime)")
//    fun getAccountTransactionChartExpense(accountId: Long, timeFrom: Calendar, timeTo: Calendar): LiveData<List<AccountTransactionChartDataObject>>
//
//    @Query("SELECT date(transactionTime) AS accountTransactionDate, SUM(transactionAmount) AS accountTransactionAmount FROM `transaction` WHERE transactionType = 'INCOME' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId = :accountId GROUP BY date(transactionTime)")
//    fun getAccountTransactionChartIncome(accountId: Long, timeFrom: Calendar, timeTo: Calendar): LiveData<List<AccountTransactionChartDataObject>>
//
//    @Query("SELECT date(transactionTime) AS accountTransactionDate, SUM(transactionAmount) AS accountTransactionAmount FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountTransferTo = :accountId GROUP BY date(transactionTime)")
//    fun getAccountTransactionChartTransferIn(accountId: Long, timeFrom: Calendar, timeTo: Calendar): LiveData<List<AccountTransactionChartDataObject>>
//
//    @Query("SELECT date(transactionTime) AS accountTransactionDate, SUM(transactionAmount) AS accountTransactionAmount FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId = :accountId GROUP BY date(transactionTime)")
//    fun getAccountTransactionChartTransferOut(accountId: Long, timeFrom: Calendar, timeTo: Calendar): LiveData<List<AccountTransactionChartDataObject>>

    @Query("SELECT accountTransactionDate, SUM(accountTransactionExpense) AS accountTransactionExpense, SUM(accountTransactionIncome) AS accountTransactionIncome, SUM(accountTransactionTransferIn) AS accountTransactionTransferIn, SUM(accountTransactionTransferOut) AS accountTransactionTransferOut FROM (SELECT date(transactionTime) AS accountTransactionDate, SUM(transactionAmount) AS accountTransactionExpense, 0 AS accountTransactionIncome, 0 AS accountTransactionTransferIn, 0 AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'EXPENSE' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId = :accountId GROUP BY date(transactionTime) UNION SELECT date(transactionTime) AS accountTransactionDate, 0 AS accountTransactionExpense, SUM(transactionAmount) AS accountTransactionIncome, 0 AS accountTransactionTransferIn, 0 AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'INCOME' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId = :accountId GROUP BY date(transactionTime) UNION SELECT date(transactionTime) AS accountTransactionDate, 0 AS accountTransactionExpense, 0 AS accountTransactionIncome, SUM(transactionAmount) AS accountTransactionTransferIn, 0 AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountTransferTo = :accountId GROUP BY date(transactionTime) UNION SELECT date(transactionTime) AS accountTransactionDate, 0 AS accountTransactionExpense, 0 AS accountTransactionIncome, 0 AS accountTransactionTransferIn, SUM(transactionAmount) AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId = :accountId GROUP BY date(transactionTime)) GROUP BY accountTransactionDate")
    fun getAccountTransactionChartData(accountId: Long, timeFrom: Calendar, timeTo: Calendar): LiveData<List<AccountTransactionChartDataObject>>

    @Query("SELECT SUM(transactionAmount) FROM `transaction` WHERE transactionType = 'INCOME'")
    fun getTotalIncome(): LiveData<Double>

    @Query("SELECT account.accountId, accountName, SUM(accountIncome) AS accountIncome, SUM(accountExpense) AS accountExpense, SUM(accountTransferIn) AS accountTransferIn, SUM(accountTransferOut) AS accountTransferOut FROM account LEFT JOIN (SELECT transactionAccountId AS accountId, SUM(CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END) AS accountIncome, SUM(CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END) AS accountExpense, 0 AS accountTransferIn, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferOut FROM `transaction` WHERE transactionTime < :timeTo GROUP BY transactionAccountId UNION SELECT transactionAccountTransferTo AS accountId, 0 AS accountIncome, 0 AS accountExpense, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferIn, 0 AS accountTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime < :timeTo GROUP BY transactionAccountTransferTo) AS tbl ON account.accountId = tbl.accountId GROUP BY account.accountId")
    fun getAccountListDataPrevMonth(timeTo: Calendar): LiveData<List<AccountListAdapterDataObject>>
}