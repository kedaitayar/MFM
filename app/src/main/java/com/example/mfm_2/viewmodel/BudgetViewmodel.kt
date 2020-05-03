package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.Budget
import com.example.mfm_2.repo.BudgetRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BudgetViewModel (application: Application): AndroidViewModel(application){
    private val repo: BudgetRepo
    val allBudget: LiveData<List<Budget>>

    init {
        val budget = MFMDatabase.getDatabase(application, viewModelScope).budgetDao()
        repo = BudgetRepo(budget)
        allBudget = repo.allBudget
    }

    fun insert(budget: Budget) = viewModelScope.launch {
        repo.insert(budget)
    }

    suspend fun delete(budget: Budget): Int{
        return repo.delete(budget)
    }

    suspend fun insertWithResult(budget: Budget): Long {
        return repo.insert(budget)
    }

    suspend fun getAllBudgetName(): List<String>{
        return runBlocking {
            repo.getAllBudgetName()
        }
    }

    suspend fun getAllBudget2(): List<Budget>{
        return repo.getAllBudget2()
    }

    suspend fun getBudget(budgetName: String): Budget{
        return repo.getBudget(budgetName)
    }
}