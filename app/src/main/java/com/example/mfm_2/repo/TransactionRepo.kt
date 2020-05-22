package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.TransactionDao
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.AccountListAdapterDataObject
import com.example.mfm_2.pojo.AccountTransactionChartDataObject
import com.example.mfm_2.pojo.TransactionListAdapterDataObject
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.util.*

class TransactionRepo(private val transactionDao: TransactionDao) {
    val allTransaction: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val accountIncome: LiveData<List<Transaction>> = transactionDao.getAccountIncomeLV()
    val accountExpense: LiveData<List<Transaction>> = transactionDao.getAccountExpenseLV()
    val accountTransfer: LiveData<List<Transaction>> = transactionDao.getAccountTransferLV()
    val accountListData: LiveData<List<AccountListAdapterDataObject>> = transactionDao.getAccountListData()
    val transactionListData: LiveData<List<TransactionListAdapterDataObject>> = transactionDao.getTransactionListData()
    val totalIncome: LiveData<Double> = transactionDao.getTotalIncome()

    suspend fun insert(transaction: Transaction): Long {
        return transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction): Int {
        return transactionDao.update(transaction)
    }

    suspend fun delete(transaction: Transaction): Int {
        return transactionDao.delete(transaction)
    }

    suspend fun getTransactionById(transactionId: Long): Transaction {
        return transactionDao.getTransactionById(transactionId)
    }

    suspend fun getTransactionGrpByBudget(time: Calendar, time2: Calendar): List<Transaction> {
        return transactionDao.getTransactionGrpByBudget(time, time2)
    }

    fun getTransactionGrpByBudget2(time: Calendar, time2: Calendar): List<Transaction> {
        return transactionDao.getTransactionGrpByBudget2(time, time2)
    }

    suspend fun getAccountIncome(): List<Transaction> {
        return transactionDao.getAccountIncome()
    }

    suspend fun getAccountExpense(): List<Transaction> {
        return transactionDao.getAccountExpense()
    }

    suspend fun getAccountTransfer(): List<Transaction> {
        return transactionDao.getAccountTransfer()
    }

    suspend fun getTime(): List<String> {
        return transactionDao.getTime()
    }

//    fun getAccountTransactionChartExpense(accountId: Long, month: Int, year: Int): LiveData<List<AccountTransactionChartDataObject>> {
//        val timeFrom = Calendar.getInstance()
//        timeFrom.set(year,month-1,1,0,0,0)
//        val timeTo = Calendar.getInstance()
//        timeTo.timeInMillis = timeFrom.timeInMillis
//        timeTo.add(Calendar.MONTH, 1)
//        timeTo.add(Calendar.SECOND, -1)
//        return transactionDao.getAccountTransactionChartExpense(accountId, timeFrom, timeTo)
//    }
//
//    fun getAccountTransactionChartIncome(accountId: Long, month: Int, year: Int): LiveData<List<AccountTransactionChartDataObject>> {
//        val timeFrom = Calendar.getInstance()
//        timeFrom.set(year,month-1,1,0,0,0)
//        val timeTo = Calendar.getInstance()
//        timeTo.timeInMillis = timeFrom.timeInMillis
//        timeTo.add(Calendar.MONTH, 1)
//        timeTo.add(Calendar.SECOND, -1)
//        return transactionDao.getAccountTransactionChartIncome(accountId, timeFrom, timeTo)
//    }
//
//    fun getAccountTransactionChartTransferIn(accountId: Long, month: Int, year: Int): LiveData<List<AccountTransactionChartDataObject>> {
//        val timeFrom = Calendar.getInstance()
//        timeFrom.set(year,month-1,1,0,0,0)
//        val timeTo = Calendar.getInstance()
//        timeTo.timeInMillis = timeFrom.timeInMillis
//        timeTo.add(Calendar.MONTH, 1)
//        timeTo.add(Calendar.SECOND, -1)
//        return transactionDao.getAccountTransactionChartTransferIn(accountId, timeFrom, timeTo)
//    }
//
//    fun getAccountTransactionChartTransferOut(accountId: Long, month: Int, year: Int): LiveData<List<AccountTransactionChartDataObject>> {
//        val timeFrom = Calendar.getInstance()
//        timeFrom.set(year,month-1,1,0,0,0)
//        val timeTo = Calendar.getInstance()
//        timeTo.timeInMillis = timeFrom.timeInMillis
//        timeTo.add(Calendar.MONTH, 1)
//        timeTo.add(Calendar.SECOND, -1)
//        return transactionDao.getAccountTransactionChartTransferOut(accountId, timeFrom, timeTo)
//    }

        fun getAccountTransactionChartData(accountId: Long, month: Int, year: Int): LiveData<List<AccountTransactionChartDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year,month-1,1,0,0,0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return transactionDao.getAccountTransactionChartData(accountId, timeFrom, timeTo)
    }

    fun getAccountListDataPrevMonth(month: Int, year: Int): LiveData<List<AccountListAdapterDataObject>> {
        val timeTo = Calendar.getInstance()
        timeTo.set(year,month-1,1,0,0,0)
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return transactionDao.getAccountListDataPrevMonth(timeTo)
    }
}