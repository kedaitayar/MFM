package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.BudgetDao
import com.example.mfm_2.model.Budget

class BudgetRepo (private val budgetDao: BudgetDao){
    val allBudget: LiveData<List<Budget>> = budgetDao.getAllBudget()

    suspend fun insert(budget: Budget): Long{
        return budgetDao.insert(budget)
    }

    suspend fun getAllBudgetName(): List<String>{
        return budgetDao.getAllBudgetName()
    }

    suspend fun getAllBudget2(): List<Budget>{
        return budgetDao.getAllBudget2()
    }

    suspend fun getBudget(budgetName: String): Budget{
        return budgetDao.getBudget(budgetName)
    }
}