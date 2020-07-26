package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.BudgetTypeDao
import com.example.mfm_2.entity.BudgetType

class BudgetTypeRepo(private val budgetTypeDao: BudgetTypeDao) {
    val allBudgetType: LiveData<List<BudgetType>> = budgetTypeDao.getAllBudgetTypeLV()

    suspend fun insert(budgetType: BudgetType): Long {
        return budgetTypeDao.insert(budgetType)
    }

    suspend fun update(budgetType: BudgetType): Int {
        return budgetTypeDao.update(budgetType)
    }

    suspend fun delete(budgetType: BudgetType): Int {
        return budgetTypeDao.delete(budgetType)
    }

    suspend fun getAllBudgetType(): List<BudgetType> {
        return budgetTypeDao.getAllBudgetType()
    }
}