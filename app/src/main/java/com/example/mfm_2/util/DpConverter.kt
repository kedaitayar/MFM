package com.example.mfm_2.util

import android.content.res.Resources

object DpConverter {
    fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()
    fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}