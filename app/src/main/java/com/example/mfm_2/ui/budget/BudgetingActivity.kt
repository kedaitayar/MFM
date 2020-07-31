package com.example.mfm_2.ui.budget

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.ui.budget.adapter.BudgetingListAdapter
import com.example.mfm_2.entity.BudgetTransaction
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
        mfmViewModel.budgetingListData.observe(this, Observer {
            it?.let {
                budgetingAdapter.submitList(it)
            }
        })
        mfmViewModel.allBudgetDeadline.observe(this, Observer {
            it?.let {
                budgetingAdapter.submitDeadlineData(it)
            }
        })
        mfmViewModel.selectedDate.observe(this, Observer {
            it?.let {
                budgetingAdapter.submitSelectedDate(it)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_budgeting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_budgeting -> {
                val budgetList = budgetingAdapter.currentList
                val date = mfmViewModel.selectedDate.value!!
                for ((index, budget) in budgetList.withIndex()) {
                    if (budget.budgetTypeId != 2L) {
                        val budgetTransaction = BudgetTransaction(
                            budgetTransactionAmount = ((budgetingAdapter.budgetAlloc[index]?.toDouble()) ?: 0.0),
                            budgetTransactionBudgetId = budget.budgetId,
                            budgetTransactionMonth = date.month,
                            budgetTransactionYear = date.year
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            mfmViewModel.insert(budgetTransaction)
                        }
                    } else {
                        val budgetTransaction = BudgetTransaction(
                            budgetTransactionAmount = ((budgetingAdapter.budgetAlloc[index]?.toDouble()) ?: 0.0),
                            budgetTransactionBudgetId = budget.budgetId,
                            budgetTransactionMonth = 0,
                            budgetTransactionYear = date.year
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            mfmViewModel.insert(budgetTransaction)
                        }
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
            R.id.budgeting_info -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Budgeting")
                    .setMessage("TODO")
                    .setPositiveButton("OK") { dialog, which -> }
                val dialog = builder.create()
                dialog.show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
