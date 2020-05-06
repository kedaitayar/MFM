package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mfm_2.pojo.SelectedDate

class SelectedDateViewModel(application: Application) : AndroidViewModel(application) {
    val selectedDate = SelectedDate()
}