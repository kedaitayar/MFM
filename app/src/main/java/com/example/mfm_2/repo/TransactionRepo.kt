package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.TransactionDao
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.AccountListAdapterDataObject
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.util.*

class TransactionRepo(private val transactionDao: TransactionDao) {
    val allTransaction: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val accountIncome: LiveData<List<Transaction>> = transactionDao.getAccountIncomeLV()
    val accountExpense: LiveData<List<Transaction>> = transactionDao.getAccountExpenseLV()
    val accountTransfer: LiveData<List<Transaction>> = transactionDao.getAccountTransferLV()
    val accountListData: LiveData<List<AccountListAdapterDataObject>> = transactionDao.getAccountListData()

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
}