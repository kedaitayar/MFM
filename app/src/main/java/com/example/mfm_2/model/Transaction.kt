package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = arrayOf("accountId"),
            childColumns = arrayOf("transactionAccountTransferTo"),
            onDelete = ForeignKey.CASCADE
        )
    ),
    indices = [Index(value = ["transactionAccountId", "transactionBudgetId", "transactionAccountTransferTo"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var transactionId: Long = 0,
    var transactionAmount: Double = 0.0,
    var transactionTime: Calendar = Calendar.getInstance(),
    var transactionType: String = "",
    var transactionAccountId: Long = 0,
    var transactionBudgetId: Long? = null,
    var transactionAccountTransferTo: Long? = null
)