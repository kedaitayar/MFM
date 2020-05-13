package com.example.mfm_2.pojo

import java.util.*

data class SelectedDate2(
    var month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    var year: Int = Calendar.getInstance().get(Calendar.YEAR)
)