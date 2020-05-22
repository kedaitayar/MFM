package com.example.mfm_2.fragment.budget

import android.app.Activity
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
import com.example.mfm_2.viewmodel.MFMViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BudgetingActivity : AppCompatActivity() {
    private lateinit var budgetingAdapter: BudgetingListAdapter
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budgeting)
        val toolbar: Toolbar = findViewById(R.id.toolbar_budgeting)
        setSupportActionBar(toolbar)
        title = "Budgeting"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerview_budgeting)
        budgetingAdapter = BudgetingListAdapter(this)
        recyclerView.adapter = budgetingAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        mfmViewModel.budgetListData.observe(this, Observer {
            it?.let { budgetingAdapter.submitList(it) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_budgeting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_budgeting -> {
//                val date = SelectedDateSingleton.singletonSelectedDate
//                val budget = budgetingAdapter.getData()
                val budget = budgetingAdapter.currentList
                val date = mfmViewModel.selectedDate.value!!
//                val budget = mfmViewModel.allBudget.value!!
                for ((index, budget) in budget.withIndex()) {
                    val budgetTransaction = BudgetTransaction(
                        budgetTransactionAmount = ((budgetingAdapter.budgetAlloc[index]?.toDouble()) ?: 0.0),
                        budgetTransactionBudgetId = budget.budgetId,
                        budgetTransactionMonth = date.month,
                        budgetTransactionYear = date.year
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        mfmViewModel.insert(budgetTransaction)
//                        budgetViewModel.insertTransaction(budgetTransaction)
//                        val date = SelectedDateSingleton.singletonSelectedDate
//                        budgetViewModel.getBudgetTransactionByDateLV(date.month, date.year)
                    }
                }
                setResult(Activity.RESULT_OK)
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
}
