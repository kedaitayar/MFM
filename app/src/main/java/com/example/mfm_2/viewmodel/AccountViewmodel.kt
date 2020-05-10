package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.Account
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.repo.AccountRepo
import com.example.mfm_2.repo.TransactionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: AccountRepo
    private val transactionRepo: TransactionRepo
    val allAccount: LiveData<List<Account>>
    private val _accountTransaction: MutableLiveData<List<Transaction>> = MutableLiveData()
    val accountTransaction: LiveData<List<Transaction>>
        get() = _accountTransaction
    private val uiScope = CoroutineScope(Dispatchers.IO)

    init {
        val database = MFMDatabase.getDatabase(application, viewModelScope)
        repo = AccountRepo(database.accountDao())
        transactionRepo = TransactionRepo(database.transactionDao())
        allAccount = repo.allAccount
    }

    fun insert(account: Account) = viewModelScope.launch {
        repo.insert(account)
    }

    suspend fun insertWithResult(account: Account): Long {
        return repo.insertWithResult(account)
    }

    suspend fun delete(account: Account): Int {
        return repo.delete(account)
    }

    fun update(account: Account) = viewModelScope.launch {
        repo.update(account)
    }

    suspend fun updateWithResult(account: Account): Int {
        return repo.updateWithResult(account)
    }

    suspend fun getAllAccountNameBG(): List<String> {
        val result = runBlocking {
            repo.getAllAccountName()
        }
        return result
    }

    suspend fun getAllAccountName(): List<String> {
        return repo.getAllAccountName()
    }

    fun getAccount(accountName: String): Account {
        return runBlocking {
            repo.getAccount(accountName)
        }
    }

    suspend fun getAccountById(accountId: Long): Account {
        return repo.getAccountById(accountId)
    }
}