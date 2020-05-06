package com.example.mfm_2.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mfm_2.model.BudgetTransaction

@Dao
interface BudgetTransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budgetTransaction: BudgetTransaction): Long

    @Query("SELECT * FROM BudgetTransaction WHERE budgetTransactionDate = :date")
    suspend fun getBudgetTransactionByDate(date: String): BudgetTransaction
}