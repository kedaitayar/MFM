package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mfm_2.model.Debt

@Dao
interface DebtDao {
    @Query("SELECT * FROM debt")
    fun getAllDebt(): LiveData<List<Debt>>

    @Insert
    suspend fun insert(debt: Debt)

    @Query("Delete from debt")
    suspend fun deleteAll()
}