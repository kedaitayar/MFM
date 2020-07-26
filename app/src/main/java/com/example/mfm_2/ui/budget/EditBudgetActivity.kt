package com.example.mfm_2.ui.budget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.entity.Budget
import com.example.mfm_2.entity.BudgetDeadline
import com.example.mfm_2.entity.BudgetType
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


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
        val budgetDeadline: TextInputEditText = findViewById(R.id.textedit_budget_deadline)
        val budgetDeadlineLayout: TextInputLayout = findViewById(R.id.textlayout_budget_deadline)
        var dropdownPos: Int = -1

        val builder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        val picker: MaterialDatePicker<*> = builder.build()
        var budgetDeadlineCalendar: Calendar? = null

        val budgetId = intent.getLongExtra(EXTRA_BUDGET_ID, -1)
        if (budgetId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                val budget = mfmViewModel.getBudgetWithBudgetTypeById(budgetId)
                dropdownPos = budget.budgetTypeId.toInt() - 1
                val budgetType: List<BudgetType> = mfmViewModel.getAllBudgetType()
                var budgetDeadlineData: BudgetDeadline
                budgetDeadlineData = when (budget.budgetTypeId) {
                    3L -> {
                        mfmViewModel.getBudgetDeadlineById(budget.budgetId) ?: BudgetDeadline(budgetId = -1L)
                    }
                    else -> BudgetDeadline(budgetId = -1L)
                }
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
                withContext(Dispatchers.Main) {
                    budgetName.setText(budget.budgetName)
                    budgetGoal.setText(budget.budgetGoal.toString())
                    budgetTypeDropdown.setAdapter(budgetTypeArrayAdapter)
                    budgetTypeDropdown.setText(budget.budgetTypeName)
                    if (budget.budgetTypeId == 3L) {
                        budgetDeadlineLayout.visibility = View.VISIBLE
                        if (budgetDeadlineData.budgetId != -1L) {
                            budgetDeadlineCalendar = budgetDeadlineData.budgetDeadline
                            val sdf = SimpleDateFormat("MMM d, yyyy")
                            budgetDeadlineData.budgetDeadline?.time.let {
                                budgetDeadline.setText(sdf.format(it!!))
                            }
                        }
                    }
                }
            }
        }

        budgetTypeDropdown.setOnItemClickListener { parent, view, position, id ->
            dropdownPos = position
            val item = parent.adapter.getItem(position) as BudgetType
            if (item.budgetTypeId == 3) {
                budgetDeadlineLayout.visibility = View.VISIBLE
            } else {
                budgetDeadlineLayout.visibility = View.GONE
            }
        }

        buttonSave.setOnClickListener {
            if (budgetName.text.isNullOrBlank() || budgetGoal.text.isNullOrBlank()) {
                val mainView: ConstraintLayout? = findViewById(R.id.main_layout)
                mainView?.let {
                    Snackbar.make(it, "All field need to be fill", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                val budget = Budget(
                    budgetId = budgetId,
                    budgetName = budgetName.text.toString(),
                    budgetGoal = budgetGoal.text.toString().toDouble(),
                    budgetType = budgetTypeArrayAdapter.getItem(dropdownPos)!!.budgetTypeId
                )
                val replyIntent = Intent()
                CoroutineScope(Dispatchers.IO).launch {
                    val resultCode = mfmViewModel.update(budget)
                    if (budget.budgetType == 3) {
                        mfmViewModel.insert(BudgetDeadline(budgetId = budget.budgetId, budgetDeadline = budgetDeadlineCalendar))
                    }
                    replyIntent.putExtra(EXTRA_UPDATE_RESULT, resultCode)
                    setResult(Activity.RESULT_OK, replyIntent)
                    finish()
                }
            }
        }

        budgetDeadline.setOnClickListener {
            picker.show(supportFragmentManager, picker.toString())
        }

        picker.addOnPositiveButtonClickListener {
            budgetDeadlineCalendar = Calendar.getInstance()
            budgetDeadlineCalendar?.timeInMillis = it as Long
            budgetDeadline.setText(picker.headerText)
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
