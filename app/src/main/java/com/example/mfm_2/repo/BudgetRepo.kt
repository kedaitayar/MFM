package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.BudgetDao
import com.example.mfm_2.model.Budget
import com.example.mfm_2.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.pojo.BudgetWithBudgetType
import java.util.*

class BudgetRepo(private val budgetDao: BudgetDao) {
    val allBudget: LiveData<List<Budget>> = budgetDao.getAllBudget()

    suspend fun insert(budget: Budget): Long {
        return budgetDao.insert(budget)
    }

    suspend fun update(budget: Budget): Int {
        return budgetDao.update(budget)
    }

    suspend fun delete(budget: Budget): Int {
        return budgetDao.delete(budget)
    }

    suspend fun getAllBudgetName(): List<String> {
        return budgetDao.getAllBudgetName()
    }

    suspend fun getAllBudget2(): List<Budget> {
        return budgetDao.getAllBudget2()
    }

    suspend fun getBudget(budgetName: String): Budget {
        return budgetDao.getBudget(budgetName)
    }

    suspend fun getBudgetById(budgetId: Long): Budget {
        return budgetDao.getBudgetById(budgetId)
    }

    suspend fun getBudgetWithBudgetTypeById(budgetId: Long): BudgetWithBudgetType {
        return budgetDao.getBudgetWithBudgetTypeById(budgetId)
    }

    fun getBudgetListAdapterDO(month: Int, year: Int): LiveData<List<BudgetListAdapterDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year,month-1,1,0,0,0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return budgetDao.getBudgetListAdapterDO(month, year, timeFrom, timeTo)
//        return budgetDao.getBudgetListAdapterDO(timeFrom, timeTo)
//        return budgetDao.getBudgetListAdapterDO()
    }
}