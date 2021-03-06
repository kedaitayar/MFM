package com.example.mfm_2.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["budgetName"], unique = true)])
data class Budget(
    @PrimaryKey(autoGenerate = true)
    var budgetId: Long = 0,
//    var budgetAllocation: Double = 0.0,
    var budgetGoal: Double = 0.0,
    var budgetName: String = "",
    var budgetType: Int = 1,
    @Ignore var isExpanded: Boolean = false

//   var budgetStartDate: String = ""
)