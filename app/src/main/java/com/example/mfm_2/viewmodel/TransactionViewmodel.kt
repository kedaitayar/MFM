package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import com.example.mfm_2.repo.TransactionRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TransactionViewModel (application: Application): AndroidViewModel(application){
    private val repo : TransactionRepo
    val allTransaction: LiveData<List<Transaction>>
    val allTransaction2: LiveData<List<TransactionWithAccountBudget>>

    init {
        val transaction = MFMDatabase.getDatabase(application, viewModelScope).transactionDao()
        repo = TransactionRepo(transaction)
        allTransaction = repo.allTransaction
        allTransaction2 = repo.allTransaction2
    }

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repo.insert(transaction)
    }

    fun update(transaction: Transaction) = viewModelScope.launch {
        repo.update(transaction)
    }

//    fun getTransactionWithAccount(): List<TransactionWithAccountAndBudget>{
//        return runBlocking {
//            repo.getTransactionWithAccount()
//        }
//    }
//
//    fun getTransactionWithAccount2(): List<TransactionWithAccountAndBudget>{
//        return runBlocking {
//            repo.getTransactionWithAccount2()
//        }
//    }
//
//    suspend fun getTransactionWithAccountBudget(): List<TransactionWithAccountBudget>{
//        return repo.getTransactionWithAccountBudget()
//    }
}