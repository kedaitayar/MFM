package com.example.mfm_2.pojo

data class AccountTransactionChartDataObject(
    var accountTransactionDate: String,
    var accountTransactionExpense: Double,
    var accountTransactionIncome: Double,
    var accountTransactionTransferIn: Double,
    var accountTransactionTransferOut: Double
)