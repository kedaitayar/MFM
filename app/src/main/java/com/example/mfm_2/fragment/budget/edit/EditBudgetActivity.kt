package com.example.mfm_2.fragment.budget.edit

import android.os.Bundle
import android.text.method.TextKeyListener
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Filter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.model.BudgetType
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditBudgetActivity : AppCompatActivity() {
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var budgetName: TextInputEditText
    private lateinit var budgetGoal: TextInputEditText
    private lateinit var budgetTypeDropdown: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        setContentView(R.layout.activity_edit_budget)
        val toolbar: Toolbar = findViewById(R.id.toolbar_edit_budget)
        setSupportActionBar(toolbar)
        title = "Edit Budget"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        budgetName = findViewById(R.id.textedit_budget_name)
        budgetGoal = findViewById(R.id.textedit_budget_goal)
        budgetTypeDropdown = findViewById(R.id.dropdown_budget_type)

        val budgetId = intent.getLongExtra(EXTRA_BUDGET_ID, -1)
        if (budgetId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                val budget = budgetViewModel.getBudgetFromId(budgetId)
                val budgetType: List<BudgetType> = budgetViewModel.getAllBudgetType()
                val budgetTypeArrayAdapter =
                    object : ArrayAdapter<BudgetType>(this@EditBudgetActivity, R.layout.dropdown_menu_popup_item, budgetType) {
                        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                            val view: View? = convertView ?: LayoutInflater.from(this@EditBudgetActivity.baseContext)
                                .inflate(R.layout.dropdown_menu_popup_item, parent, false)
                            val text = view as TextView
                            text.text = getItem(position)!!.budgetTypeName

                            return view
                        }

                        override fun getFilter(): Filter {
                            return ArrayFilter()
                        }

                        inner class ArrayFilter : Filter() {
                            override fun performFiltering(prefix: CharSequence): FilterResults {
                                return FilterResults()
                            }

                            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                            }

                            override fun convertResultToString(resultValue: Any?): CharSequence {
                                val result = resultValue as BudgetType
                                return result.budgetTypeName
                            }
                        }
                    }
//                val budgetTypeArrayAdapter = BudgetTypeDropdownAdapter(this@EditBudgetActivity, R.layout.dropdown_menu_popup_item,0, budgetType)
                withContext(Dispatchers.Main) {
                    budgetName.setText(budget.budgetName)
                    budgetGoal.setText(budget.budgetGoal.toString())
                    budgetTypeDropdown.setAdapter(budgetTypeArrayAdapter)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_budget, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_budget -> {
                Log.i("haha", budgetName.text.toString())
                Log.i("haha", budgetGoal.text.toString())
                Log.i("haha", budgetTypeDropdown.text.toString())
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
        const val EXTRA_BUDGET_NAME = "com.example.mfm_2.fragment.budget.edit.EXTRA_BUDGET_NAME"
        const val EXTRA_BUDGET_ID = "com.example.mfm_2.fragment.budget.edit.EXTRA_BUDGET_ID"
    }
}
