package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["budgetTypeName"], unique = true)])
data class BudgetType(
    @PrimaryKey(autoGenerate = true)
    var budgetTypeId: Int = 0,
    var budgetTypeName: String = ""
)