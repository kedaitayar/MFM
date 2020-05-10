package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("accountId"),
            childColumns = arrayOf("transactionAccountId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Budget::class,
            parentColumns = arrayOf("budgetId"),
            childColumns = arrayOf("transactionBudgetId"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Transaction(
    var transactionAccount: String = "",
    var transactionAmount: Double = 0.0,
    var transactionTime: Calendar = Calendar.getInstance(),
    var transactionType: String = "",
    var transactionAccountId: Long = 0,
    var transactionBudgetId: Long = 0,
    var transactionAccountTransferTo: Long = 0,
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long = 0
)