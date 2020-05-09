package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.BudgetTypeDao
import com.example.mfm_2.model.BudgetType

class BudgetTypeRepo (private val budgetTypeDao: BudgetTypeDao){
    val allBudgetType: LiveData<List<BudgetType>> = budgetTypeDao.getAllBudgetTypeLV()

    suspend fun getAllBudgetType(): List<BudgetType> {
        return budgetTypeDao.getAllBudgetType()
    }
}