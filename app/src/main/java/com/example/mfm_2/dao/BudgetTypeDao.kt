package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mfm_2.model.BudgetType

@Dao
interface BudgetTypeDao {
    @Query("SELECT * FROM budgettype")
    fun getAllBudgetType(): List<BudgetType>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budgetType: BudgetType): Long

    @Query("Delete from budgettype")
    suspend fun deleteAll()
}