package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BudgetTransaction(
    @PrimaryKey(autoGenerate = true)
    var budgetTransactionId: Long = 0,
    var budgetTransactionAmount: Float = 0f,
    var budgetTransactionMonth: Int = -1,
    var budgetTransactionYear: Int = -1,
    var budgetTransactionBudgetId: Long = -1
)