package com.example.mfm_2.repo

import com.example.mfm_2.dao.BudgetTransactionDao
import com.example.mfm_2.model.BudgetTransaction

class BudgetTransactionRepo(
    private val budgetTransactionDao: BudgetTransactionDao
) {
    suspend fun insert(budgetTransaction: BudgetTransaction): Long{
        return budgetTransactionDao.insert(budgetTransaction)
    }
}