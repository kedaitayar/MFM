package com.example.mfm_2.pojo

import java.util.*

data class TransactionWithAccountBudget(
//    var transactionAccount: String = "",
//    var transactionBudget: String = "",
//    var transactionDebt: String = "",
//    var transactionAmount: Int = 0,
//    var transactionTime: Calendar = Calendar.getInstance(),
//    var transactionType: String = "",
//    var transactionId: Long = 0

    var transactionAmount: Double = 0.0,
    var transactionId: Long = -1,
    var accountName: String = "",
    var budgetName: String = "",
    var transactionTime: Calendar = Calendar.getInstance(),
    var transactionType: String = ""
)