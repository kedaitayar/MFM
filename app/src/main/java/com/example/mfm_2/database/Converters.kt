package com.example.mfm_2.database

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class Converters {
//    @TypeConverter
//    fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis
//
//    @TypeConverter
//    fun datestampToCalendar(value: Long): Calendar =
//        Calendar.getInstance().apply { timeInMillis = value }

    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
    @TypeConverter
    fun calendarToISO8601(calendar: Calendar): String {
        val date = calendar.time
        return sdf.format(date)
    }

    @TypeConverter
    fun iso8601ToCalendar(value: String): Calendar {
        val date = sdf.parse(value)
        val calendar = Calendar.getInstance()
        date?.let {
            calendar.time = date
            return calendar
        }
        calendar.set(Calendar.YEAR, 2222)
        return calendar
    }
}