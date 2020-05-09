package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.model.Budget
import com.example.mfm_2.pojo.BudgetWithBudgetType

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget")
    fun getAllBudget(): LiveData<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budget: Budget): Long

    @Update
    suspend fun update(budget: Budget): Int

    @Delete
    suspend fun delete(budget: Budget): Int

    @Query("Delete from budget")
    suspend fun deleteAll()

    @Query("SELECT budgetName FROM budget")
    suspend fun getAllBudgetName(): List<String>

    @Query("SELECT * FROM budget")
    suspend fun getAllBudget2(): List<Budget>

    @Query("SELECT * FROM budget WHERE budgetName = :budgetName")
    suspend fun getBudget(budgetName: String): Budget

    @Query("SELECT * FROM budget WHERE budgetId = :budgetId")
    suspend fun getBudgetById(budgetId: Long): Budget

    @Query("SELECT budgetId, budgetAllocation, budgetGoal, budgetName, budgetTypeId, budgetTypeName FROM budget INNER JOIN budgettype ON budget.budgetType = budgettype.budgetTypeId WHERE budgetId = :budgetId")
    suspend fun getBudgetWithBudgetTypeById(budgetId: Long): BudgetWithBudgetType
}