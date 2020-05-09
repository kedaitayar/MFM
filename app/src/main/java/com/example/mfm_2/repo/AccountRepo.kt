package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.AccountDao
import com.example.mfm_2.model.Account

class AccountRepo(private val accountDao: AccountDao) {
    val allAccount: LiveData<List<Account>> = accountDao.getAllAccount()

    suspend fun insert(account: Account) {
        accountDao.insert(account)
    }

    suspend fun insertWithResult(account: Account): Long {
        return accountDao.insert(account)
    }

    suspend fun getAllAccountName(): List<String> {
        return accountDao.getAllAccountName()
    }

    suspend fun getAccount(accountName: String): Account {
        return accountDao.getAccount(accountName)
    }

    suspend fun update(account: Account) {
        accountDao.update(account)
    }

    suspend fun updateWithResult(account: Account): Int {
        return accountDao.update(account)
    }

    suspend fun getAccountById(accountId: Long): Account {
        return accountDao.getAccountById(accountId)
    }
}