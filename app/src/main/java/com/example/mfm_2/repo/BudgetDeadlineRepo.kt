package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.BudgetDeadlineDao
import com.example.mfm_2.entity.BudgetDeadline

class BudgetDeadlineRepo(private val budgetDeadlineDao: BudgetDeadlineDao) {
    val allBudgetDeadline: LiveData<List<BudgetDeadline>> = budgetDeadlineDao.getAllBudgetDeadline()

    suspend fun getBudgetDeadlineById(budgetId: Long): BudgetDeadline? {
        return budgetDeadlineDao.getBudgetDeadlineById(budgetId)
    }

    suspend fun insert(budgetDeadline: BudgetDeadline): Long {
        return budgetDeadlineDao.insert(budgetDeadline)
    }

    suspend fun update(budgetDeadline: BudgetDeadline): Int {
        return budgetDeadlineDao.update(budgetDeadline)
    }

    suspend fun delete(budgetDeadline: BudgetDeadline): Int {
        return budgetDeadlineDao.delete(budgetDeadline)
    }
}