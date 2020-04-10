package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.Account
import com.example.mfm_2.repo.AccountRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AccountViewModel(application: Application): AndroidViewModel(application){
    private val repo: AccountRepo
    val allAccount: LiveData<List<Account>>
    private val uiScope = CoroutineScope(Dispatchers.IO)

    init {
        val accountDao = MFMDatabase.getDatabase(application, viewModelScope).accountDao()
        repo = AccountRepo(accountDao)
        allAccount = repo.allAccount
    }

    fun insert(account: Account) = viewModelScope.launch{
        repo.insert(account)
    }

    fun update(account: Account) = viewModelScope.launch{
        repo.update(account)
    }

    suspend fun getAllAccountNameBG(): List<String>{
        val result = runBlocking {
            repo.getAllAccountName()
        }
        return result
    }

    suspend fun getAllAccountName(): List<String>{
        return repo.getAllAccountName()
    }

    fun getAccount(accountName: String): Account {
        return runBlocking {
            repo.getAccount(accountName)
        }
    }
}