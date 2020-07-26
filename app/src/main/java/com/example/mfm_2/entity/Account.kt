package com.example.mfm_2.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["accountName"], unique = true)])
data class Account(
    @PrimaryKey(autoGenerate = true)
    var accountId: Long = 0,
    var accountName: String = ""
)