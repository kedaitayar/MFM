package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BudgetTransaction(
    @PrimaryKey(autoGenerate = true)
    var budgetTransactionId: Long = -1,
    var budgetTransactionAmount: Float = 0f,
    var budgetTransactionDate: String = "",
    var budgetTransactionBudget: Long = -1
)