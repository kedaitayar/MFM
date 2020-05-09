package com.example.mfm_2.repo

import androidx.lifecycle.LiveData
import com.example.mfm_2.dao.BudgetTransactionDao
import com.example.mfm_2.model.BudgetTransaction

class BudgetTransactionRepo(private val budgetTransactionDao: BudgetTransactionDao) {

    suspend fun insert(budgetTransaction: BudgetTransaction): Long {
        return budgetTransactionDao.insert(budgetTransaction)
    }

    suspend fun getBudgetTransactionByDate(month: Int, year: Int): List<BudgetTransaction> {
        return budgetTransactionDao.getBudgetTransactionByDate(month, year)
    }

    fun getBudgetTransactionByDateLV(month: Int, year: Int): LiveData<List<BudgetTransaction>> {
        return budgetTransactionDao.getBudgetTransactionByDateLV(month, year)
    }
}