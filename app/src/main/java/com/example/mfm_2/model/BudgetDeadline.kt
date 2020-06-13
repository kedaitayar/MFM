package com.example.mfm_2.model

import androidx.room.PrimaryKey
import java.util.*

data class BudgetDeadline(
    @PrimaryKey
    val budgetId: Long,
    val budgetDeadline: Calendar
)