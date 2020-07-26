package com.example.mfm_2.singleton

import com.example.mfm_2.util.pojo.SelectedDate2
import java.util.*

object SelectedDateSingleton {
    val singletonSelectedDate: SelectedDate2 by lazy {
        val cal = Calendar.getInstance()
        SelectedDate2(month = cal.get(Calendar.MONTH)+1, year = cal.get(Calendar.YEAR))
    }
}