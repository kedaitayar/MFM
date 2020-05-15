package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    primaryKeys = ["budgetTransactionMonth", "budgetTransactionYear", "budgetTransactionBudgetId"],
    foreignKeys = [ForeignKey(
        entity = Budget::class,
        parentColumns = arrayOf("budgetId"),
        childColumns = arrayOf("budgetTransactionBudgetId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class BudgetTransaction(
//    @PrimaryKey(autoGenerate = true)
//    var budgetTransactionId: Long = 0,
    var budgetTransactionMonth: Int = -1,
    var budgetTransactionYear: Int = -1,
    var budgetTransactionAmount: Double = 0.0,
    var budgetTransactionBudgetId: Long = -1
)