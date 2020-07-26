package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.TransactionDao
import com.example.mfm_2.entity.Transaction
import com.example.mfm_2.util.pojo.*
import java.util.*

class TransactionRepo(private val transactionDao: TransactionDao) {
    val accountListData: LiveData<List<AccountListAdapterDataObject>> = transactionDao.getAccountListData()
    val transactionListData: LiveData<List<TransactionListAdapterDataObject>> = transactionDao.getTransactionListData()
    val transactionGraphData: LiveData<List<TransactionGraphDataObject>> = transactionDao.getTransactionGraphData()
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

    suspend fun getAccountIncome(): List<Transaction> {
        return transactionDao.getAccountIncome()
    }

    suspend fun getAccountExpense(): List<Transaction> {
        return transactionDao.getAccountExpense()
    }

    suspend fun getAccountTransfer(): List<Transaction> {
        return transactionDao.getAccountTransfer()
    }

    fun getAccountTransactionChartData(accountId: Long, month: Int, year: Int): LiveData<List<AccountTransactionChartDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year, month - 1, 1, 0, 0, 0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        val timeToPrevMonth = Calendar.getInstance()
        timeToPrevMonth.set(year, month - 1, 1, 0, 0, 0)
        timeToPrevMonth.add(Calendar.SECOND, -1)
        return transactionDao.getAccountTransactionChartData(accountId, timeFrom, timeTo, timeToPrevMonth)
    }

    fun getAccountTransactionList(accountId: Long): LiveData<List<TransactionListAdapterDataObject>>? {
        return transactionDao.getAccountTransactionListData(accountId)
    }
}