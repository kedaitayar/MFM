package com.example.mfm_2.repo

import com.example.mfm_2.dao.BudgetDeadlineDao
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.BudgetDeadline

class BudgetDeadlineRepo(private val budgetDeadlineDao: BudgetDeadlineDao) {
    suspend fun getAllBudgetDeadline(): List<BudgetDeadline> {
        return budgetDeadlineDao.getAllBudgetDeadline()
    }

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