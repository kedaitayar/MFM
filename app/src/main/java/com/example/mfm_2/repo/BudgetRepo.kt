package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.BudgetDao
import com.example.mfm_2.entity.Budget
import com.example.mfm_2.util.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.util.pojo.BudgetPieChartDataObject
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

    suspend fun getBudgetWithBudgetTypeById(budgetId: Long): BudgetListAdapterDataObject {
        return budgetDao.getBudgetWithBudgetTypeById(budgetId)
    }

    fun getBudgetingListAdapterDO(month: Int, year: Int): LiveData<List<BudgetListAdapterDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year, month - 1, 1, 0, 0, 0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return budgetDao.getBudgetingListAdapterDO(month, year, timeFrom, timeTo)
    }

    fun getBudgetGoalDebtListAdapterDO(month: Int, year: Int): LiveData<List<BudgetListAdapterDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year, month - 1, 1, 0, 0, 0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return budgetDao.getBudgetGoalDebtListAdapterDO(month, year, timeFrom, timeTo)
    }

    fun getBudgetListAdapterDO(month: Int, year: Int): LiveData<List<BudgetListAdapterDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year, month - 1, 1, 0, 0, 0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return budgetDao.getBudgetListAdapterDO(month, year, timeFrom, timeTo)
    }

    fun getBudgetListAdapterDO(month: Int, year: Int, budgetType: Int): LiveData<List<BudgetListAdapterDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year, month - 1, 1, 0, 0, 0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return budgetDao.getBudgetListAdapterDO(month, year, timeFrom, timeTo, budgetType)
    }

    fun getBudgetMonthlyListAdapterDO(month: Int, year: Int): LiveData<List<BudgetListAdapterDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year, month - 1, 1, 0, 0, 0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 1)
        timeTo.add(Calendar.SECOND, -1)
        return budgetDao.getBudgetMonthlyListAdapterDO(month, year, timeFrom, timeTo)
    }

    fun getBudgetYearlyListAdapterDO(year: Int): LiveData<List<BudgetListAdapterDataObject>> {
        val timeFrom = Calendar.getInstance()
        timeFrom.set(year, 0, 1, 0, 0, 0)
        val timeTo = Calendar.getInstance()
        timeTo.timeInMillis = timeFrom.timeInMillis
        timeTo.add(Calendar.MONTH, 12)
        timeTo.add(Calendar.SECOND, -1)
        return budgetDao.getBudgetYearlyListAdapterDO(year, timeFrom, timeTo)
    }

    suspend fun getBudgetDetail(): List<BudgetPieChartDataObject> {
        return budgetDao.getBudgetDetail()
    }

    suspend fun getBudgetDetail(budgetType: List<Long>): List<BudgetPieChartDataObject> {
        return budgetDao.getBudgetDetail(budgetType)
    }

    suspend fun getBudgetDetail(timeFrom: Calendar, timeTo: Calendar): List<BudgetPieChartDataObject> {
        return budgetDao.getBudgetDetail(timeFrom, timeTo)
    }

    suspend fun getBudgetDetail(timeFrom: Calendar, timeTo: Calendar, budgetType: List<Long>): List<BudgetPieChartDataObject> {
        return budgetDao.getBudgetDetail(timeFrom, timeTo, budgetType)
    }
}