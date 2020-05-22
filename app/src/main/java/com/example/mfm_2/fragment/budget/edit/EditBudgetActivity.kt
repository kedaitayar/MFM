package com.example.mfm_2.fragment.budget.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.TextKeyListener
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.BudgetType
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class EditBudgetActivity : AppCompatActivity() {
    private lateinit var budgetTypeArrayAdapter: ArrayAdapter<BudgetType>
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)

        setContentView(R.layout.activity_edit_budget)
        val toolbar: Toolbar = findViewById(R.id.toolbar_edit_budget)
        setSupportActionBar(toolbar)
        title = "Edit Budget"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val budgetName: TextInputEditText = findViewById(R.id.textedit_budget_name)
        val budgetGoal: TextInputEditText = findViewById(R.id.textedit_budget_goal)
        val budgetTypeDropdown: AutoCompleteTextView = findViewById(R.id.dropdown_budget_type)
        val buttonSave: Button = findViewById(R.id.button_save)
        var dropdownPos: Int = -1

        val budgetId = intent.getLongExtra(EXTRA_BUDGET_ID, -1)
        if (budgetId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                val budget = mfmViewModel.getBudgetWithBudgetTypeById(budgetId)
                dropdownPos = budget.budgetTypeId.toInt()
                val budgetType: List<BudgetType> = mfmViewModel.getAllBudgetType()
                budgetTypeArrayAdapter =
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
                    if (budget.budgetGoal != 0.0){
                        budgetGoal.setText(budget.budgetGoal.toString())
                    }
                    budgetTypeDropdown.setAdapter(budgetTypeArrayAdapter)
                    budgetTypeDropdown.setText(budget.budgetTypeName)
                }
            }
        }

        budgetTypeDropdown.setOnItemClickListener { parent, view, position, id ->
            dropdownPos = position
        }

        buttonSave.setOnClickListener {
            val budget = Budget(budgetId = budgetId, budgetName = budgetName.text.toString(), budgetGoal = budgetGoal.text.toString().toDouble(), budgetType = budgetTypeArrayAdapter.getItem(dropdownPos)!!.budgetTypeId)
            val replyIntent = Intent()
            CoroutineScope(Dispatchers.IO).launch {
                val resultCode = mfmViewModel.update(budget)
                replyIntent.putExtra(EXTRA_UPDATE_RESULT, resultCode)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
        const val EXTRA_UPDATE_RESULT = "com.example.mfm_2.fragment.budget.edit.EXTRA_UPDATE_RESULT"
    }
}
