package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mfm_2.util.pojo.SelectedDate2

object SelectedDateRepo {
    val selectedDate: LiveData<SelectedDate2>
        get() = _selectedDate
    private val _selectedDate = MutableLiveData<SelectedDate2>()

    init {
        _selectedDate.postValue(SelectedDate2())
    }

    fun setSelectedDate(month: Int, year: Int){
        _selectedDate.postValue(SelectedDate2(month = month, year = year))
    }
}