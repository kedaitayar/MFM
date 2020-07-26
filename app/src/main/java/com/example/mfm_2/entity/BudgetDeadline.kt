package com.example.mfm_2.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class BudgetDeadline(
    @PrimaryKey
    val budgetId: Long = -1L,
    val budgetDeadline: Calendar? = null
)