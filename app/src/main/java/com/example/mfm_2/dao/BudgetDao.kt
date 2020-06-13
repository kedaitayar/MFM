package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.model.Budget
import com.example.mfm_2.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.pojo.BudgetPieChartDataObject
import com.example.mfm_2.pojo.BudgetWithBudgetType
import java.util.*

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget")
    fun getAllBudget(): LiveData<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(budget: Budget): Long

    @Update
    suspend fun update(budget: Budget): Int

    @Delete
    suspend fun delete(budget: Budget): Int

    @Query("Delete from budget")
    suspend fun deleteAll()

    @Query("SELECT budgetName FROM budget")
    suspend fun getAllBudgetName(): List<String>

    @Query("SELECT * FROM budget")
    suspend fun getAllBudget2(): List<Budget>

    @Query("SELECT * FROM budget WHERE budgetName = :budgetName")
    suspend fun getBudget(budgetName: String): Budget

    @Query("SELECT * FROM budget WHERE budgetId = :budgetId")
    suspend fun getBudgetById(budgetId: Long): Budget

    @Query("SELECT budgetId, 0 AS budgetAllocation, budgetGoal, budgetName, budgetTypeId, budgetTypeName, 0 AS budgetUsed, 0 AS isExpanded FROM budget INNER JOIN budgettype ON budget.budgetType = budgettype.budgetTypeId WHERE budgetId = :budgetId")
    suspend fun getBudgetWithBudgetTypeById(budgetId: Long): BudgetListAdapterDataObject

//    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year")
//    fun getBudgetListAdapterDO(month: Int, year: Int): LiveData<List<BudgetListAdapterDataObject>>

//    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN (SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId) AS tbl ON budgetId = transactionBudgetId")
//    fun getBudgetListAdapterDO(timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

//        @Query("SELECT budgetId, budgetName, budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetType AS budgetTypeId, 0 AS budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId UNION ALL SELECT budgetId, budgetName, budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetType AS budgetTypeId, 0 AS budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE budgetTransactionBudgetId IS NOT NULL")
//    fun getBudgetListAdapterDO(): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN (SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId) AS tbl ON budgetId = transactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, 0 AS budgetGoal, 0 AS budgetUsed, budgetType AS budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId WHERE NOT EXISTS (SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId AND budgetTransactionMonth = :month AND budgetTransactionYear = :year)")
    fun getBudgetListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN (SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId) AS tbl ON budgetId = transactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year AND budgetType = :budgetType UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, 0 AS budgetGoal, 0 AS budgetUsed, budgetType AS budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId WHERE NOT EXISTS (SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId AND budgetTransactionMonth = :month AND budgetTransactionYear = :year) AND budgetType = :budgetType")
    fun getBudgetListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar, budgetType: Int): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, SUM(transactionAmount) AS budgetTransactionTotal FROM `transaction` INNER JOIN budget ON transactionBudgetId = budgetId GROUP BY budgetId")
    suspend fun getBudgetDetail(): List<BudgetPieChartDataObject>

    @Query("SELECT budgetId, budgetName, SUM(transactionAmount) AS budgetTransactionTotal FROM `transaction` INNER JOIN budget ON transactionBudgetId = budgetId WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY budgetId")
    suspend fun getBudgetDetail(timeFrom: Calendar, timeTo: Calendar): List<BudgetPieChartDataObject>
}