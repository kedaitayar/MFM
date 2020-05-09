package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.mfm_2.model.Account

@Dao
interface AccountDao {
    @Query("SELECT * FROM account")
    fun getAllAccount(): LiveData<List<Account>>

    @Insert
    suspend fun insert(account: Account): Long

    @Update
    suspend fun update(account: Account): Int

    @Query("Delete from account")
    suspend fun deleteAll()

    @Query("SELECT accountName FROM account")
    suspend fun getAllAccountName(): List<String>

    @Query("SELECT * FROM account WHERE accountName = :accountName")
    suspend fun getAccount(accountName: String): Account

    @Query("SELECT * FROM account WHERE accountId = :accountId")
    suspend fun getAccountById(accountId: Long): Account
}