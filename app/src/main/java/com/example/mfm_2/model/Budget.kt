package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Budget(
    @PrimaryKey(autoGenerate = true)
    var budgetId: Long = 0,
    var budgetAllocation: Double = 0.0,
    var budgetCycle: String = "",
    var budgetGoal: Double = 0.0,
    var budgetName: String = ""
//   var budgetStartDate: String = ""
)