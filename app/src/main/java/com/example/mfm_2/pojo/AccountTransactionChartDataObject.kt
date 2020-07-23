package com.example.mfm_2.pojo

data class AccountTransactionChartDataObject(
    var accountTransactionDate: String,
    var accountTransactionExpense: Double,
    var accountTransactionIncome: Double,
    var accountTransactionTransferIn: Double,
    var accountTransactionTransferOut: Double,
    var accountTransactionIncomePrevMonth: Double?,
    var accountTransactionExpensePrevMonth: Double?,
    var accountTransactionTransferInPrevMonth: Double?,
    var accountTransactionTransferOutPrevMonth: Double?
)