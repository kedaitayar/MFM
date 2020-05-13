package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.model.BudgetType

@Dao
interface BudgetTypeDao {
    @Query("SELECT * FROM budgettype")
    fun getAllBudgetType(): List<BudgetType>

    @Query("SELECT * FROM budgettype")
    fun getAllBudgetTypeLV(): LiveData<List<BudgetType>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budgetType: BudgetType): Long

    @Update
    suspend fun update(budgetType: BudgetType): Int

    @Delete
    suspend fun delete(budgetType: BudgetType): Int

    @Query("Delete from budgettype")
    suspend fun deleteAll()
}