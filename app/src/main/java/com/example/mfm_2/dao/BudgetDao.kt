package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mfm_2.model.Budget

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget")
    fun getAllBudget(): LiveData<List<Budget>>

    @Insert
    suspend fun insert(budget: Budget)

    @Query("Delete from budget")
    suspend fun deleteAll()

    @Query("SELECT budgetName FROM budget")
    suspend fun getAllBudgetName(): List<String>

    @Query("SELECT * FROM budget")
    suspend fun getAllBudget2(): List<Budget>

    @Query("SELECT * FROM budget WHERE budgetName = :budgetName")
    suspend fun getBudget(budgetName: String): Budget
}