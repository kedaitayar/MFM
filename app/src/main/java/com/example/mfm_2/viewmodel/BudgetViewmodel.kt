package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.BudgetType
import com.example.mfm_2.repo.BudgetRepo
import com.example.mfm_2.repo.BudgetTypeRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class BudgetViewModel (application: Application): AndroidViewModel(application){
    private val repo: BudgetRepo
    private val typeRepo: BudgetTypeRepo
    val allBudget: LiveData<List<Budget>>

    init {
        val database = MFMDatabase.getDatabase(application, viewModelScope)
        repo = BudgetRepo(database.budgetDao())
        typeRepo = BudgetTypeRepo(database.budgetTypeDao())
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

    suspend fun getBudgetFromId(budgetId: Long): Budget{
        return repo.getBudgetFromId(budgetId)
    }

    suspend fun getAllBudgetType(): List<BudgetType>{
        return typeRepo.getAllBudgetType()
    }
}