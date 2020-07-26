package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.entity.Transaction
import com.example.mfm_2.util.pojo.*
import java.util.*

@Dao
interface TransactionDao {
    @Query("SELECT * FROM `Transaction` ORDER BY transactionId DESC")
    fun getAllTransaction(): LiveData<List<Transaction>>

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

    @Query("SELECT *, account.accountName AS transactionAccountName, budget.budgetName AS transactionBudgetName, account2.accountName AS transactionAccountTransferToName FROM `transaction` LEFT JOIN account ON transactionAccountId = account.accountId LEFT JOIN budget ON transactionBudgetId = budget.budgetId LEFT JOIN account AS account2 ON transactionAccountTransferTo = account2.accountId ORDER BY transactionId DESC")
    fun getTransactionListData(): LiveData<List<TransactionListAdapterDataObject>>

    @Query("SELECT STRFTIME('%W', transactionTime) AS transactionWeek, Sum(transactionAmount) AS transactionAmount, transactionType FROM `transaction` WHERE transactionType != 'TRANSFER' GROUP BY STRFTIME('%W', transactionTime), transactionType ORDER BY STRFTIME('%W', transactionTime)")
    fun getTransactionGraphData(): LiveData<List<TransactionGraphDataObject>>

    @Query("SELECT accountTransactionDate, SUM(accountTransactionExpense) AS accountTransactionExpense, SUM(accountTransactionIncome) AS accountTransactionIncome, SUM(accountTransactionTransferIn) AS accountTransactionTransferIn, SUM(accountTransactionTransferOut) AS accountTransactionTransferOut, ( SELECT SUM( CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END ) AS accountTransactionIncomePrevMonth FROM `transaction` WHERE transactionTime < :timeToPrevMonth AND transactionAccountId = :accountId GROUP BY transactionAccountId ) AS accountTransactionIncomePrevMonth, ( SELECT SUM( CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END ) AS accountTransactionExpensePrevMonth FROM `transaction` WHERE transactionTime < :timeToPrevMonth AND transactionAccountId =  :accountId GROUP BY transactionAccountId ) AS accountTransactionExpensePrevMonth, ( SELECT SUM( CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END ) AS accountTransactionTransferInPrevMonth FROM `transaction` WHERE transactionTime < :timeToPrevMonth AND transactionAccountTransferTo =  :accountId GROUP BY transactionAccountTransferTo ) AS accountTransactionTransferInPrevMonth, ( SELECT SUM( CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END ) AS accountTransactionTransferOutPrevMonth FROM `transaction` WHERE transactionTime < :timeToPrevMonth AND transactionAccountId =  :accountId GROUP BY transactionAccountId ) AS accountTransactionTransferOutPrevMonth FROM ( SELECT date(transactionTime) AS accountTransactionDate, SUM(transactionAmount) AS accountTransactionExpense, 0 AS accountTransactionIncome, 0 AS accountTransactionTransferIn, 0 AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'EXPENSE' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId =  :accountId GROUP BY date(transactionTime) UNION SELECT date(transactionTime) AS accountTransactionDate, 0 AS accountTransactionExpense, SUM(transactionAmount) AS accountTransactionIncome, 0 AS accountTransactionTransferIn, 0 AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'INCOME' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId =  :accountId GROUP BY date(transactionTime) UNION SELECT date(transactionTime) AS accountTransactionDate, 0 AS accountTransactionExpense, 0 AS accountTransactionIncome, SUM(transactionAmount) AS accountTransactionTransferIn, 0 AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId =  :accountId GROUP BY date(transactionTime) UNION SELECT date(transactionTime) AS accountTransactionDate, 0 AS accountTransactionExpense, 0 AS accountTransactionIncome, 0 AS accountTransactionTransferIn, SUM(transactionAmount) AS accountTransactionTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime BETWEEN :timeFrom AND :timeTo AND transactionAccountId =  :accountId GROUP BY date(transactionTime) ) GROUP BY accountTransactionDate")
    fun getAccountTransactionChartData(accountId: Long, timeFrom: Calendar, timeTo: Calendar, timeToPrevMonth: Calendar): LiveData<List<AccountTransactionChartDataObject>>

    @Query("SELECT SUM(transactionAmount) FROM `transaction` WHERE transactionType = 'INCOME'")
    fun getTotalIncome(): LiveData<Double>

    @Query("SELECT account.accountId, accountName, SUM(accountIncome) AS accountIncome, SUM(accountExpense) AS accountExpense, SUM(accountTransferIn) AS accountTransferIn, SUM(accountTransferOut) AS accountTransferOut FROM account LEFT JOIN (SELECT transactionAccountId AS accountId, SUM(CASE WHEN transactionType = 'INCOME' THEN transactionAmount ELSE 0 END) AS accountIncome, SUM(CASE WHEN transactionType = 'EXPENSE' THEN transactionAmount ELSE 0 END) AS accountExpense, 0 AS accountTransferIn, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferOut FROM `transaction` WHERE transactionTime < :timeTo GROUP BY transactionAccountId UNION SELECT transactionAccountTransferTo AS accountId, 0 AS accountIncome, 0 AS accountExpense, SUM(CASE WHEN transactionType = 'TRANSFER' THEN transactionAmount ELSE 0 END) AS accountTransferIn, 0 AS accountTransferOut FROM `transaction` WHERE transactionType = 'TRANSFER' AND transactionTime < :timeTo GROUP BY transactionAccountTransferTo) AS tbl ON account.accountId = tbl.accountId GROUP BY account.accountId")
    fun getAccountListDataPrevMonth(timeTo: Calendar): LiveData<List<AccountListAdapterDataObject>>

    @Query("SELECT *, account.accountName AS transactionAccountName, budget.budgetName AS transactionBudgetName, account2.accountName  AS transactionAccountTransferToName  FROM `transaction` LEFT JOIN account ON transactionAccountId = account.accountId LEFT JOIN budget ON transactionBudgetId = budget.budgetId LEFT JOIN account AS account2 ON transactionAccountTransferTo = account2.accountId WHERE account.accountId = :accountId ORDER BY transactionId DESC LIMIT 10")
    fun getAccountTransactionListData(accountId: Long): LiveData<List<TransactionListAdapterDataObject>>
}