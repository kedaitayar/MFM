package com.example.mfm_2.pojo

import java.util.*

class SelectedDate {
    var month: Int
    var year: Int

    init {
        val cal = Calendar.getInstance()
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }
}