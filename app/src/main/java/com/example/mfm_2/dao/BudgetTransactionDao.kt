package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.entity.BudgetTransaction

@Dao
interface BudgetTransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetTransaction: BudgetTransaction): Long

    @Update
    suspend fun update(budgetTransaction: BudgetTransaction): Int

    @Delete
    suspend fun delete(budgetTransaction: BudgetTransaction): Int

    @Query("SELECT * FROM BudgetTransaction WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year GROUP BY budgetTransactionMonth, budgetTransactionYear, budgetTransactionBudgetId")
    suspend fun getBudgetTransactionByDate(month: Int, year: Int): List<BudgetTransaction>

    @Query("SELECT * FROM BudgetTransaction WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year GROUP BY budgetTransactionMonth, budgetTransactionYear, budgetTransactionBudgetId")
    fun getBudgetTransactionByDateLV(month: Int, year: Int): LiveData<List<BudgetTransaction>>

    @Query("Delete from budgettransaction")
    suspend fun deleteAll()

    @Query("SELECT * FROM budgettransaction")
    fun getAllBudgetTransaction(): LiveData<List<BudgetTransaction>>

    @Query("SELECT SUM(budgetTransactionAmount) FROM budgettransaction")
    fun getTotalBudgetedAmount(): LiveData<Double>
}