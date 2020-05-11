package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.BudgetTransaction
import com.example.mfm_2.model.BudgetType
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.BudgetWithBudgetType
import com.example.mfm_2.repo.BudgetRepo
import com.example.mfm_2.repo.BudgetTransactionRepo
import com.example.mfm_2.repo.BudgetTypeRepo
import com.example.mfm_2.repo.TransactionRepo
import com.example.mfm_2.singleton.SelectedDateSingleton
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class BudgetViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: BudgetRepo
    private val typeRepo: BudgetTypeRepo
    private val budgetTransactionRepo: BudgetTransactionRepo
    private val transactionRepo: TransactionRepo
    val allBudget: LiveData<List<Budget>>
    val allBudgetType: LiveData<List<BudgetType>>
    val allTransaction: LiveData<List<Transaction>>
    private val _budgetTransaction: MutableLiveData<List<BudgetTransaction>> = MutableLiveData()
    val budgetTransaction: LiveData<List<BudgetTransaction>>
        get() = _budgetTransaction
    private val _transaction: MutableLiveData<List<Transaction>> = MutableLiveData()
    val transaction: LiveData<List<Transaction>>
        get() = _transaction

    init {
        val database = MFMDatabase.getDatabase(application, viewModelScope)
        repo = BudgetRepo(database.budgetDao())
        typeRepo = BudgetTypeRepo(database.budgetTypeDao())
        budgetTransactionRepo = BudgetTransactionRepo(database.budgetTransactionDao())
        transactionRepo = TransactionRepo(database.transactionDao())
        allBudget = repo.allBudget
        allBudgetType = typeRepo.allBudgetType
        allTransaction = transactionRepo.allTransaction
    }

    fun insert(budget: Budget) = viewModelScope.launch {
        repo.insert(budget)
    }

    fun update(budget: Budget) = viewModelScope.launch {
        repo.update(budget)
    }

    suspend fun updateWithResult(budget: Budget): Int {
        return repo.update(budget)
    }

    suspend fun delete(budget: Budget): Int {
        return repo.delete(budget)
    }

    suspend fun insertWithResult(budget: Budget): Long {
        return repo.insert(budget)
    }

    suspend fun getAllBudgetName(): List<String> {
        return runBlocking {
            repo.getAllBudgetName()
        }
    }

    suspend fun getAllBudget2(): List<Budget> {
        return repo.getAllBudget2()
    }

    suspend fun getBudget(budgetName: String): Budget {
        return repo.getBudget(budgetName)
    }

    suspend fun getBudgetById(budgetId: Long): Budget {
        return repo.getBudgetById(budgetId)
    }

    suspend fun getBudgetWithBudgetTypeById(budgetId: Long): BudgetWithBudgetType {
        return repo.getBudgetWithBudgetTypeById(budgetId)
    }

    suspend fun getAllBudgetType(): List<BudgetType> {
        return typeRepo.getAllBudgetType()
    }

    //BudgetTransaction
    fun insertTransaction(budgetTransaction: BudgetTransaction) = viewModelScope.launch {
        budgetTransactionRepo.insert(budgetTransaction)
    }

    suspend fun getBudgetTransactionByDate(month: Int, year: Int): List<BudgetTransaction> {
        return budgetTransactionRepo.getBudgetTransactionByDate(month, year)
    }

    suspend fun getBudgetTransactionByDateLV(month: Int, year: Int) {
        _budgetTransaction.postValue(budgetTransactionRepo.getBudgetTransactionByDate(month, year))
    }

    suspend fun getTransactionGrpByBudget(time: Calendar, time2: Calendar): List<Transaction> {
        return transactionRepo.getTransactionGrpByBudget(time, time2)
    }

    suspend fun getTransactionGrpByBudgetLV(time: Calendar, time2: Calendar) {
        _transaction.postValue(transactionRepo.getTransactionGrpByBudget(time, time2))
    }
}