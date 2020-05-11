package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.TransactionDao
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.util.*

class TransactionRepo(private val transactionDao: TransactionDao) {
    val allTransaction: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
//    val allTransaction2: LiveData<List<TransactionWithAccountBudget>> = transactionDao.getAllTransaction2()

    suspend fun insert(transaction: Transaction) {
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction) {
        transactionDao.update(transaction)
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
}