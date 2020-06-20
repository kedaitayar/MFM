package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.model.BudgetDeadline

@Dao
interface BudgetDeadlineDao {
    @Query("SELECT * FROM budgetdeadline")
    suspend fun getAllBudgetDeadline(): List<BudgetDeadline>

    @Query("SELECT * FROM budgetdeadline WHERE budgetId = :budgetId")
    suspend fun getBudgetDeadlineById(budgetId: Long): BudgetDeadline?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budgetDeadline: BudgetDeadline): Long

    @Update
    suspend fun update(budgetDeadline: BudgetDeadline): Int

    @Delete
    suspend fun delete(budgetDeadline: BudgetDeadline): Int

    @Query("Delete from budgetdeadline")
    suspend fun deleteAll()
}