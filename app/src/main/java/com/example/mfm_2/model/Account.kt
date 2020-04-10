package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Account(
    @PrimaryKey(autoGenerate = true)
    var accountId: Long = 0,
    var accountName: String = "",
    var accountBalance: Double = 0.0,
    var accountAllocationBalance: Double = 0.0
)