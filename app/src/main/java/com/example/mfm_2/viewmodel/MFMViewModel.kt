package com.example.mfm_2.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.mfm_2.database.MFMDatabase
import com.example.mfm_2.model.*
import com.example.mfm_2.pojo.*
import com.example.mfm_2.repo.*
import java.util.*

class MFMViewModel(application: Application) : AndroidViewModel(application) {
    //repo
    private val accountRepo: AccountRepo
    private val budgetRepo: BudgetRepo
    private val transactionRepo: TransactionRepo
    private val budgetTransactionRepo: BudgetTransactionRepo
    private val budgetTypeRepo: BudgetTypeRepo
    private val budgetDeadlineRepo: BudgetDeadlineRepo
    private val selectedDateRepo = SelectedDateRepo

    //simple livedata
    val allAccount: LiveData<List<Account>>
    val allBudget: LiveData<List<Budget>>

    //    val allTransaction: LiveData<List<Transaction>>
    val allBudgetTransaction: LiveData<List<BudgetTransaction>>
    val allBudgetType: LiveData<List<BudgetType>>
    val allBudgetDeadline: LiveData<List<BudgetDeadline>>
    val accountListData: LiveData<List<AccountListAdapterDataObject>>
    val transactionListData: LiveData<List<TransactionListAdapterDataObject>>
    val selectedDate: LiveData<SelectedDate2>

    //    val allBudgetTransactionByDate: LiveData<List<BudgetTransaction>>
    val budgetListData: LiveData<List<BudgetListAdapterDataObject>>
    val budgetingListData: LiveData<List<BudgetListAdapterDataObject>>
    val monthlyBudgetListData: LiveData<List<BudgetListAdapterDataObject>>
    val yearlyBudgetListData: LiveData<List<BudgetListAdapterDataObject>>
    val debtBudgetListData: LiveData<List<BudgetListAdapterDataObject>>
    val totalBudgetedAmount: LiveData<Double>
    val totalIncome: LiveData<Double>
    val accountListDataPrevMonth: LiveData<List<AccountListAdapterDataObject>>

    //    val accountTransactionChartExpense: LiveData<List<AccountTransactionChartDataObject>>
//    val accountTransactionChartIncome: LiveData<List<AccountTransactionChartDataObject>>
//    val accountTransactionChartTransferIn: LiveData<List<AccountTransactionChartDataObject>>
//    val accountTransactionChartTransferOut: LiveData<List<AccountTransactionChartDataObject>>
    val accountTransactionChartData: LiveData<List<AccountTransactionChartDataObject>>

    //non room livedata
    val selectedAccount: LiveData<Long>
        get() = _selectedAccount
    private val _selectedAccount = MutableLiveData<Long>()

    class SelectedAccountAndSelectedDateTrigger<Long, SelectedDate2>(selectedAccount: LiveData<Long>, selectedDate: LiveData<SelectedDate2>) :
        MediatorLiveData<Pair<Long?, SelectedDate2?>>() {
        init {
            addSource(selectedAccount) {
                value = it to selectedDate.value
            }
            addSource(selectedDate) {
                value = selectedAccount.value to it
            }
        }
    }

    init {
        //repo init
        val database = MFMDatabase.getDatabase(application, viewModelScope)
        accountRepo = AccountRepo(database.accountDao())
        budgetRepo = BudgetRepo(database.budgetDao())
        transactionRepo = TransactionRepo(database.transactionDao())
        budgetTransactionRepo = BudgetTransactionRepo(database.budgetTransactionDao())
        budgetTypeRepo = BudgetTypeRepo(database.budgetTypeDao())
        budgetDeadlineRepo = BudgetDeadlineRepo(database.budgetDeadlineDao())

        //livedata init
        allAccount = accountRepo.allAccount
        allBudget = budgetRepo.allBudget
        allBudgetDeadline = budgetDeadlineRepo.allBudgetDeadline
//        allTransaction = transactionRepo.allTransaction
        allBudgetTransaction = budgetTransactionRepo.allBudgetTransaction
        allBudgetType = budgetTypeRepo.allBudgetType
        selectedDate = selectedDateRepo.selectedDate
        accountListData = transactionRepo.accountListData
        transactionListData = transactionRepo.transactionListData
//        allBudgetTransactionByDate = Transformations.switchMap(selectedDate) {
//            budgetTransactionRepo.getBudgetTransactionByDateLV(it.month, it.year)
//        }
        budgetListData = Transformations.switchMap(selectedDate) {
            budgetRepo.getBudgetListAdapterDO(it.month, it.year)
        }
        budgetingListData = Transformations.switchMap(selectedDate) {
            budgetRepo.getBudgetingListAdapterDO(it.month, it.year)
        }
        monthlyBudgetListData = Transformations.switchMap(selectedDate) {
            budgetRepo.getBudgetListAdapterDO(it.month, it.year, 1)
        }
        yearlyBudgetListData = Transformations.switchMap(selectedDate) {
            budgetRepo.getBudgetListAdapterDO(it.month, it.year, 2)
        }
        debtBudgetListData = Transformations.switchMap(selectedDate) {
            budgetRepo.getBudgetGoalDebtListAdapterDO(it.month, it.year)
        }
        totalBudgetedAmount = budgetTransactionRepo.totalBudgetedAmount
        totalIncome = transactionRepo.totalIncome
        accountListDataPrevMonth = Transformations.switchMap(selectedDate) {
            transactionRepo.getAccountListDataPrevMonth(it.month, it.year)
        }
        _selectedAccount.postValue(0)
//        accountTransactionChartExpense = Transformations.switchMap(SelectedAccountAndSelectedDateTrigger(selectedAccount, selectedDate)) {
//            transactionRepo.getAccountTransactionChartExpense(it.first?:0, it.second?.month?:0, it.second?.year?:0)
//        }
//        accountTransactionChartIncome = Transformations.switchMap(SelectedAccountAndSelectedDateTrigger(selectedAccount, selectedDate)) {
//            transactionRepo.getAccountTransactionChartIncome(it.first?:0, it.second?.month?:0, it.second?.year?:0)
//        }
//        accountTransactionChartTransferIn = Transformations.switchMap(SelectedAccountAndSelectedDateTrigger(selectedAccount, selectedDate)) {
//            transactionRepo.getAccountTransactionChartTransferIn(it.first?:0, it.second?.month?:0, it.second?.year?:0)
//        }
//        accountTransactionChartTransferOut = Transformations.switchMap(SelectedAccountAndSelectedDateTrigger(selectedAccount, selectedDate)) {
//            transactionRepo.getAccountTransactionChartTransferOut(it.first?:0, it.second?.month?:0, it.second?.year?:0)
//        }
        accountTransactionChartData = Transformations.switchMap(SelectedAccountAndSelectedDateTrigger(selectedAccount, selectedDate)) {
            transactionRepo.getAccountTransactionChartData(it.first ?: 0, it.second?.month ?: 0, it.second?.year ?: 0)
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

    suspend fun insert(budgetDeadline: BudgetDeadline): Long {
        return budgetDeadlineRepo.insert(budgetDeadline)
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

    suspend fun getBudgetById(budgetId: Long): Budget {
        return budgetRepo.getBudgetById(budgetId)
    }

    suspend fun getTransactionById(transactionId: Long): Transaction {
        return transactionRepo.getTransactionById(transactionId)
    }

    suspend fun getBudgetDeadlineById(budgetId: Long): BudgetDeadline? {
        return budgetDeadlineRepo.getBudgetDeadlineById(budgetId)
    }

    fun setSelectedDate(month: Int, year: Int) {
        selectedDateRepo.setSelectedDate(month, year)
    }

    suspend fun getBudgetWithBudgetTypeById(budgetId: Long): BudgetListAdapterDataObject {
        return budgetRepo.getBudgetWithBudgetTypeById(budgetId)
    }

    suspend fun getTime(): List<String> {
        return transactionRepo.getTime()
    }

    suspend fun getAllBudgetType(): List<BudgetType> {
        return budgetTypeRepo.getAllBudgetType()
    }

    fun setSelectedAccount(accountId: Long) {
        _selectedAccount.postValue(accountId)
    }

    suspend fun getBudgetDetail(): List<BudgetPieChartDataObject> {
        return budgetRepo.getBudgetDetail()
    }

    suspend fun getBudgetDetail(budgetType: List<Long>): List<BudgetPieChartDataObject> {
        return budgetRepo.getBudgetDetail(budgetType)
    }

    suspend fun getBudgetDetail(timeFrom: Calendar, timeTo: Calendar): List<BudgetPieChartDataObject> {
        return budgetRepo.getBudgetDetail(timeFrom, timeTo)
    }

    suspend fun getBudgetDetail(timeFrom: Calendar, timeTo: Calendar, budgetType: List<Long>): List<BudgetPieChartDataObject> {
        return budgetRepo.getBudgetDetail(timeFrom, timeTo, budgetType)
    }
}