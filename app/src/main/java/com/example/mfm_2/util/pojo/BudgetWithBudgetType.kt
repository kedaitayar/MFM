package com.example.mfm_2.util.pojo

import androidx.room.Ignore

data class BudgetWithBudgetType(
    var budgetId: Long = 0,
    var budgetAllocation: Double = 0.0,
//    var budgetCycle: String = "",
    var budgetGoal: Double = 0.0,
    var budgetName: String = "",
    var budgetTypeId: Int = 0,
    var budgetTypeName: String = "",
    @Ignore var isExpanded: Boolean = false
)