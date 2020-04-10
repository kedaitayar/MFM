package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transfer(
    @PrimaryKey(autoGenerate = true)
    var transferId: Long = 0,
    var transferFrom: String,
    var transferTo: String
)