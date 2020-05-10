package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.TransactionDao
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget

class TransactionRepo (private val transactionDao: TransactionDao){
    val allTransaction: LiveData<List<Transaction>> = transactionDao.getAllTransaction()
    val allTransaction2: LiveData<List<TransactionWithAccountBudget>> = transactionDao.getAllTransaction2()

    suspend fun insert(transaction: Transaction){
        transactionDao.insert(transaction)
    }

    suspend fun update(transaction: Transaction){
        transactionDao.update(transaction)
    }

//    suspend fun getTransactionWithAccount(): List<TransactionWithAccountAndBudget>{
//        return transactionDao.getTransactionWithAccount()
//    }
//
//    suspend fun getTransactionWithAccount2(): List<TransactionWithAccountAndBudget>{
//        return transactionDao.getTransactionWithAccount2()
//    }
//
//    suspend fun getTransactionWithAccountBudget(): List<TransactionWithAccountBudget>{
//        return transactionDao.getTransactionWithAccountBudget()
//    }
}