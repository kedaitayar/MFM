package com.example.mfm_2.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.*
import com.example.mfm_2.pojo.AccountListAdapterDataObject
import com.example.mfm_2.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.pojo.SelectedDate2
import com.example.mfm_2.pojo.TransactionListAdapterDataObject
import com.example.mfm_2.repo.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MFMViewModel(application: Application) : AndroidViewModel(application) {
    //repo
    private val accountRepo: AccountRepo
    private val budgetRepo: BudgetRepo
    private val transactionRepo: TransactionRepo
    private val budgetTransactionRepo: BudgetTransactionRepo
    private val budgetTypeRepo: BudgetTypeRepo
    private val selectedDateRepo = SelectedDateRepo

    //simple livedata
    val allAccount: LiveData<List<Account>>
    val allBudget: LiveData<List<Budget>>

    //    val allTransaction: LiveData<List<Transaction>>
    val allBudgetTransaction: LiveData<List<BudgetTransaction>>
    val accountListData: LiveData<List<AccountListAdapterDataObject>>
    val transactionListData: LiveData<List<TransactionListAdapterDataObject>>
    val selectedDate: LiveData<SelectedDate2>
//    val allBudgetTransactionByDate: LiveData<List<BudgetTransaction>>
    val budgetListData: LiveData<List<BudgetListAdapterDataObject>>

    init {
        //repo init
        val database = MFMDatabase.getDatabase(application, viewModelScope)
        accountRepo = AccountRepo(database.accountDao())
        budgetRepo = BudgetRepo(database.budgetDao())
        transactionRepo = TransactionRepo(database.transactionDao())
        budgetTransactionRepo = BudgetTransactionRepo(database.budgetTransactionDao())
        budgetTypeRepo = BudgetTypeRepo(database.budgetTypeDao())

        //livedata init
        allAccount = accountRepo.allAccount
        allBudget = budgetRepo.allBudget
//        allTransaction = transactionRepo.allTransaction
        allBudgetTransaction = budgetTransactionRepo.allBudgetTransaction
        selectedDate = selectedDateRepo.selectedDate
        accountListData = transactionRepo.accountListData
        transactionListData = transactionRepo.transactionListData
//        allBudgetTransactionByDate = Transformations.switchMap(selectedDate) {
//            budgetTransactionRepo.getBudgetTransactionByDateLV(it.month, it.year)
//        }
        budgetListData = Transformations.switchMap(selectedDate) {
            budgetRepo.getBudgetListAdapterDO(it.month, it.year)
        }
    }

    suspend fun insert(account: Account): Long {
        return accountRepo.insert(account)
    }

    suspend fun insert(budget: Budget): Long {
        return budgetRepo.insert(budget)
    }

    suspend fun insert(transaction: Transaction): Long {
        return transactionRepo.insert(transaction)
    }

    suspend fun insert(budgetTransaction: BudgetTransaction): Long {
        return budgetTransactionRepo.insert(budgetTransaction)
    }

    suspend fun insert(budgetType: BudgetType): Long {
        return budgetTypeRepo.insert(budgetType)
    }

    suspend fun update(account: Account): Int {
        return accountRepo.update(account)
    }

    suspend fun update(budget: Budget): Int {
        return budgetRepo.update(budget)
    }

    suspend fun update(transaction: Transaction): Int {
        return transactionRepo.update(transaction)
    }

    suspend fun update(budgetTransaction: BudgetTransaction): Int {
        return budgetTransactionRepo.update(budgetTransaction)
    }

    suspend fun update(budgetType: BudgetType): Int {
        return budgetTypeRepo.update(budgetType)
    }

    suspend fun delete(account: Account): Int {
        return accountRepo.delete(account)
    }

    suspend fun delete(budget: Budget): Int {
        return budgetRepo.delete(budget)
    }

    suspend fun delete(transaction: Transaction): Int {
        return transactionRepo.delete(transaction)
    }

    suspend fun delete(budgetTransaction: BudgetTransaction): Int {
        return budgetTransactionRepo.delete(budgetTransaction)
    }

    suspend fun delete(budgetType: BudgetType): Int {
        return budgetTypeRepo.delete(budgetType)
    }

    suspend fun getAccountById(accountId: Long): Account {
        return accountRepo.getAccountById(accountId)
    }

    suspend fun getTransactionById(transactionId: Long): Transaction {
        return transactionRepo.getTransactionById(transactionId)
    }

    fun setSelectedDate(month: Int, year: Int) {
        selectedDateRepo.setSelectedDate(month, year)
    }
}