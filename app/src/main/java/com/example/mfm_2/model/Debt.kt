package com.example.mfm_2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Debt(
    var debtAmount: Long = 0,
    var debtCycle: String = "",
//    var debtDue: Date
    var debtInterestRate: Double = 0.0,
    var debtInterestType: String = "",
    @PrimaryKey
    var debtName: String = "",
//    var debtStartDate: Date
    var debtStatus: String = ""
)