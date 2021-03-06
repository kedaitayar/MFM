package com.example.mfm_2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mfm_2.entity.Budget
import com.example.mfm_2.util.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.util.pojo.BudgetPieChartDataObject
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

    @Query("SELECT budgetId, 0 AS budgetAllocation, budgetGoal, budgetName, budgetTypeId, budgetTypeName, 0 AS budgetUsed, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget INNER JOIN budgettype ON budget.budgetType = budgettype.budgetTypeId WHERE budgetId = :budgetId")
    suspend fun getBudgetWithBudgetTypeById(budgetId: Long): BudgetListAdapterDataObject

//    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year")
//    fun getBudgetListAdapterDO(month: Int, year: Int): LiveData<List<BudgetListAdapterDataObject>>

//    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN (SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId) AS tbl ON budgetId = transactionBudgetId")
//    fun getBudgetListAdapterDO(timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

//        @Query("SELECT budgetId, budgetName, budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetType AS budgetTypeId, 0 AS budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId UNION ALL SELECT budgetId, budgetName, budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetType AS budgetTypeId, 0 AS budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE budgetTransactionBudgetId IS NOT NULL")
//    fun getBudgetListAdapterDO(): LiveData<List<BudgetListAdapterDataObject>>

//backup
//    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN (SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId) AS tbl ON budgetId = transactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, 0 AS budgetGoal, 0 AS budgetUsed, budgetType AS budgetTypeId, budgetTypeName, 0 AS isExpanded FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId WHERE NOT EXISTS (SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId AND budgetTransactionMonth = :month AND budgetTransactionYear = :year)")
//    fun getBudgetListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE NOT EXISTS ( SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId AND budgetTransactionMonth = :month AND budgetTransactionYear = :year )")
    fun getBudgetListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year AND budgetType = :budgetType UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE budgetType = :budgetType AND NOT EXISTS ( SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId AND budgetTransactionMonth = :month AND budgetTransactionYear = :year ) AND budgetType = :budgetType")
    fun getBudgetListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar, budgetType: Int): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE budgetTransactionMonth = :month AND budgetTransactionYear = :year AND budgetType = 1 UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE budgetType = 1 AND NOT EXISTS ( SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId AND budgetTransactionMonth = :month AND budgetTransactionYear = :year ) AND budgetType = 1")
    fun getBudgetMonthlyListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE budgetTransactionYear = :year AND budgetType = 2 UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE budgetType = 2 AND NOT EXISTS ( SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId AND budgetTransactionYear = :year ) AND budgetType = 2")
    fun getBudgetYearlyListAdapterDO(year: Int, timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, SUM(budgetAllocation) AS budgetAllocation, budgetGoal, SUM(budgetUsed) AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, SUM(budgetTotalPrevAllocation) AS budgetTotalPrevAllocation FROM ( SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` LEFT JOIN Budget ON transactionBudgetId = budgetId WHERE (transactionTime BETWEEN :timeFrom AND :timeTo AND budgetType != 2) OR (strftime('%Y', transactionTime) = :year AND budgetType = 2) GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE (budgetTransactionMonth = :month AND budgetTransactionYear = :year AND budgetTypeId != 2) OR (budgetTransactionYear = :year AND budgetTypeId = 2) UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, tbl.transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN ( SELECT transactionBudgetId, SUM(transactionAmount) AS transactionAmount FROM `transaction` LEFT JOIN Budget ON transactionBudgetId = budgetId WHERE (transactionTime BETWEEN :timeFrom AND :timeTo AND budgetType != 2) OR (strftime('%Y', transactionTime) = :year AND budgetType = 2) GROUP BY transactionBudgetId ) AS tbl ON budgetId = transactionBudgetId WHERE NOT EXISTS ( SELECT * FROM budgettransaction WHERE budget.budgetId = budgettransaction.budgetTransactionBudgetId ) UNION ALL SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, SUM(budgetTransactionAmount) AS budgetTotalPrevAllocation FROM Budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE budgetType = 3 AND budgetTransactionMonth < :month AND budgetTransactionYear <= :year GROUP BY budgetId ) GROUP BY budgetId ORDER BY budgetTypeId, budgetId")
    fun getBudgetingListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, SUM(budgetAllocation) AS budgetAllocation, budgetGoal, SUM(budgetUsed) AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, SUM(budgetTotalPrevAllocation) AS budgetTotalPrevAllocation FROM (SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, SUM(budgetTransactionAmount) AS budgetTotalPrevAllocation FROM Budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE budgetType = 3 AND budgetTransactionMonth < :month AND budgetTransactionYear <= :year GROUP BY budgetId UNION ALL SELECT budgetId, budgetName, budgetTransactionAmount AS budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM Budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE budgetType = 3 AND budgetTransactionMonth = :month AND budgetTransactionYear = :year UNION ALL SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, transactionAmount AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM `Transaction` LEFT JOIN Budget ON transactionBudgetId = budgetId LEFT JOIN budgettype ON budgetType = budgetTypeId WHERE budgetType = 3 AND transactionTime BETWEEN :timeFrom AND :timeTo UNION SELECT budgetId, budgetName, 0 AS budgetAllocation, budgetGoal, 0 AS budgetUsed, budgetTypeId, budgetTypeName, 0 AS isExpanded, 0 AS budgetTotalPrevAllocation FROM Budget LEFT JOIN budgettype ON budgetType = budgetTypeId LEFT JOIN budgettransaction ON budgetId = budgetTransactionBudgetId WHERE Budget.budgetType = 3 AND NOT EXISTS ( SELECT * FROM budgettransaction WHERE BudgetTransaction.budgetTransactionBudgetId = Budget.budgetId AND budgetTransactionMonth = :month AND budgetTransactionYear = :year)) GROUP BY budgetId")
    fun getBudgetGoalDebtListAdapterDO(month: Int, year: Int, timeFrom: Calendar, timeTo: Calendar): LiveData<List<BudgetListAdapterDataObject>>

    @Query("SELECT budgetId, budgetName, SUM(transactionAmount) AS budgetTransactionTotal FROM `transaction` INNER JOIN budget ON transactionBudgetId = budgetId GROUP BY budgetId")
    suspend fun getBudgetDetail(): List<BudgetPieChartDataObject>

    @Query("SELECT budgetId, budgetName, SUM(transactionAmount) AS budgetTransactionTotal FROM `transaction` INNER JOIN budget ON transactionBudgetId = budgetId WHERE budgetType IN (:budgetType) GROUP BY budgetId")
    suspend fun getBudgetDetail(budgetType: List<Long>): List<BudgetPieChartDataObject>

    @Query("SELECT budgetId, budgetName, SUM(transactionAmount) AS budgetTransactionTotal FROM `transaction` INNER JOIN budget ON transactionBudgetId = budgetId WHERE transactionTime BETWEEN :timeFrom AND :timeTo GROUP BY budgetId")
    suspend fun getBudgetDetail(timeFrom: Calendar, timeTo: Calendar): List<BudgetPieChartDataObject>

    @Query("SELECT budgetId, budgetName, SUM(transactionAmount) AS budgetTransactionTotal FROM `transaction` INNER JOIN budget ON transactionBudgetId = budgetId WHERE transactionTime BETWEEN :timeFrom AND :timeTo AND budgetType IN (:budgetType) GROUP BY budgetId")
    suspend fun getBudgetDetail(timeFrom: Calendar, timeTo: Calendar, budgetType: List<Long>): List<BudgetPieChartDataObject>
}