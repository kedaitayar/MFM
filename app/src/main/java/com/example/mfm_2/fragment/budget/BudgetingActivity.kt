package com.example.mfm_2.fragment.budget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.fragment.budget.adapter.BudgetingListAdapter
import com.example.mfm_2.model.BudgetTransaction
import com.example.mfm_2.singleton.SelectedDateSingleton
import com.example.mfm_2.viewmodel.BudgetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetingActivity : AppCompatActivity() {
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var budgetingAdapter: BudgetingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budgeting)
        val toolbar: Toolbar = findViewById(R.id.toolbar_budgeting)
        setSupportActionBar(toolbar)
        title = "Budgeting"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview_budgeting)
        budgetingAdapter = BudgetingListAdapter(this)
        recyclerView.adapter = budgetingAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        budgetViewModel.allBudget.observe(this, Observer { budget ->
            budget?.let { budgetingAdapter.setBudget(budget) }
        })

        CoroutineScope(Dispatchers.IO).launch {
            val date = SelectedDateSingleton.singletonSelectedDate
            budgetViewModel.getBudgetTransactionByDateLV(date.month, date.year)
        }

        budgetViewModel.budgetTransaction.observe(this, Observer {
            it?.let { budgetingAdapter.setBudgetTransaction(it) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_budgeting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_budgeting -> {
                val date = SelectedDateSingleton.singletonSelectedDate
                val budget = budgetingAdapter.getData()
                for ((index, budget) in budget.withIndex()) {
                    val budgetTransaction = BudgetTransaction(
                        budgetTransactionAmount = ((budgetingAdapter.budgetAlloc[index]?.toFloat()) ?: 0.0F),
                        budgetTransactionBudgetId = budget.budgetId,
                        budgetTransactionMonth = date.month,
                        budgetTransactionYear = date.year
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        budgetViewModel.insertTransaction(budgetTransaction)
                        val date = SelectedDateSingleton.singletonSelectedDate
                        budgetViewModel.getBudgetTransactionByDateLV(date.month, date.year)
                    }
                }
                finish()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_MONTH = "com.example.mfm_2.fragment.budget.EXTRA_MONTH"
        const val EXTRA_YEAR = "com.example.mfm_2.fragment.budget.EXTRA_YEAR"
    }
}
