package com.example.mfm_2.ui.budget

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.entity.Budget
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 */
class BudgetFragment : Fragment() {
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)

        childFragmentManager.beginTransaction().replace(R.id.fragment_container1, BudgetListFragment.newInstance(1)).commit()
        childFragmentManager.beginTransaction().replace(R.id.fragment_container2, BudgetListFragment.newInstance(2)).commit()
        childFragmentManager.beginTransaction().replace(R.id.fragment_container3, BudgetListFragment.newInstance(3)).commit()

        val addBudget: Button = view.findViewById(R.id.button_add_budget)
        addBudget.setOnClickListener {
            val budget = Budget(budgetName = "New Budget", budgetGoal = 0.0, budgetType = 1)
            CoroutineScope(IO).launch {
                val result = mfmViewModel.insert(budget)
                withContext(Main) {
                    if (result == -1L) {
                        val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                        if (mainView != null) {
                            Snackbar.make(mainView, "Budget already exist", Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
                        val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                        if (mainView != null) {
                            Snackbar.make(mainView, "New budget inserted", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        val budgetingButton: Button = view.findViewById(R.id.button_budgeting)
        budgetingButton.setOnClickListener {
            val intent = Intent(this.context, BudgetingActivity::class.java)
            startActivity(intent)
        }

        val budgetDetailButton: Button = view.findViewById(R.id.button_budget_detail)
        budgetDetailButton.setOnClickListener {
            val intent = Intent(this.context, BudgetDetailActivity::class.java)
            startActivity(intent)
        }

        val info: Button = view.findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Budget")
                .setMessage("This section is use to manage and track your budget. Budget is use to make sure you are not over spending and have enough money until your next paycheck.\n\nTo add new budget tap the add budget button. To edit the budget tap the overflow menu and select the edit option.\n\n")
                .setPositiveButton("OK"){ dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }


        view.findViewById<Button>(R.id.info_button1).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Monthly Budget")
                .setMessage("Budget with monthly cycle.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button2).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Yearly Budget")
                .setMessage("Budget with yearly cycle.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button3).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Goal/Debt")
                .setMessage("Budget with monthly cycle with a deadline. Useful if you have a goal to have a specific amount of money at specific date. The monthly goal will be calculated by the app.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button4).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Budgeted")
                .setMessage("The amount of fund that is allocated to the budget with the percentage to meet the budget goal.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button6).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Budgeted")
                .setMessage("The amount of fund that is allocated to the budget with the percentage to meet the budget goal.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button8).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Budgeted")
                .setMessage("The amount of fund that is allocated to the budget with the percentage to meet the budget goal.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button5).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Used")
                .setMessage("The amount of fund that you have used.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button7).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Used")
                .setMessage("The amount of fund that you have used.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        view.findViewById<Button>(R.id.info_button5).setOnClickListener {
            AlertDialog.Builder(this.context!!).setTitle("Used")
                .setMessage("The amount of fund that you have used.")
                .setPositiveButton("OK"){ dialog, which -> }
                .create()
                .show()
        }

        return view
    }

}
