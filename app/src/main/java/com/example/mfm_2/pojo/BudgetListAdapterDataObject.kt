package com.example.mfm_2.pojo

data class BudgetListAdapterDataObject(
    var budgetId: Long = 0,
    var budgetName: String = "",
    var budgetAllocation: Double = 0.0,
    var budgetGoal: Double = 0.0,
    var budgetUsed: Double = 0.0,
    var budgetTypeId: Long = 0,
    var budgetTypeName: String = "",
    var isExpanded: Boolean = false
)