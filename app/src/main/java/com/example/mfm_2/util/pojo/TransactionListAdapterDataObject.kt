package com.example.mfm_2.util.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class TransactionListAdapterDataObject(
    var transactionId: Long = 0,
    var transactionAmount: Double = 0.0,
    var transactionTime: Calendar = Calendar.getInstance(),
    var transactionType: String = "",
    var transactionAccountId: Long = 0,
    var transactionBudgetId: Long? = null,
    var transactionAccountTransferTo: Long? = null,
    var transactionAccountName: String = "",
    var transactionBudgetName: String? = null,
    var transactionAccountTransferToName: String? = null
) : Parcelable