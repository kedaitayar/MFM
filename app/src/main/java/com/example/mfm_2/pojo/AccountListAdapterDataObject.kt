package com.example.mfm_2.pojo

data class AccountListAdapterDataObject(
    var accountId: Long = 0,
    var accountName: String = "",
    var accountIncome: Double,
    var accountExpense: Double,
    var accountTransferIn: Double,
    var accountTransferOut: Double
)