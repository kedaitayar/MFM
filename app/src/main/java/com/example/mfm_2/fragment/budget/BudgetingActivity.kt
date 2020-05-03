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
import com.example.mfm_2.fragment.budget.adapter.BudgetListAdapter
import com.example.mfm_2.fragment.budget.adapter.BudgetingListAdapter
import com.example.mfm_2.viewmodel.BudgetViewModel

class BudgetingActivity : AppCompatActivity() {
    private lateinit var budgetViewModel: BudgetViewModel

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
        val budgetingAdapter = BudgetingListAdapter(this)
//        val budgetingAdapter = BudgetListAdapter(this)
        recyclerView.adapter = budgetingAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        budgetViewModel.allBudget.observe(this, Observer { budget ->
            budget?.let { budgetingAdapter.setData(it) }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_budgeting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.save_budgeting -> {
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
