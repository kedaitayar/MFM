package com.example.mfm_2.util.pojo

import java.util.*

class SelectedDate {
    var month: Int
    var year: Int

    init {
        val cal = Calendar.getInstance()
        month = cal.get(Calendar.MONTH) + 1
        year = cal.get(Calendar.YEAR)
    }
}