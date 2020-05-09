package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mfm_2.model.BudgetTransaction

@Dao
interface BudgetTransactionDao {
    @Insert
    suspend fun insert(budgetTransaction: BudgetTransaction): Long

    @Query("SELECT * FROM BudgetTransaction WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year GROUP BY budgetTransactionMonth, budgetTransactionYear, budgetTransactionBudgetId")
    suspend fun getBudgetTransactionByDate(month: Int, year: Int): List<BudgetTransaction>

    @Query("SELECT * FROM BudgetTransaction WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year GROUP BY budgetTransactionMonth, budgetTransactionYear, budgetTransactionBudgetId")
    fun getBudgetTransactionByDateLV(month: Int, year: Int): LiveData<List<BudgetTransaction>>

    @Query("Delete from budgettransaction")
    suspend fun deleteAll()
}