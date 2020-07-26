package com.example.mfm_2.util.pojo

data class BudgetPieChartDataObject(
    var budgetId: Long,
    var budgetName: String? = "",
    var budgetTransactionTotal: Double = 0.0
)