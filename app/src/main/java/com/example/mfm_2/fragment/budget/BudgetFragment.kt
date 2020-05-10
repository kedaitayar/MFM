package com.example.mfm_2.fragment.budget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.fragment.budget.adapter.BudgetListAdapter
import com.example.mfm_2.fragment.budget.edit.EditBudgetActivity
import com.example.mfm_2.model.Budget
import com.example.mfm_2.singleton.SelectedDateSingleton
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 */
class BudgetFragment : Fragment() {
    private lateinit var budgetViewModel: BudgetViewModel
    private val editBudgetCode = 1
    private val editBudgetingCode = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)
//        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        budgetViewModel = activity?.run { ViewModelProvider(this).get(BudgetViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_budget)
        val budgetAdapter = BudgetListAdapter(this.context!!)
        recyclerView.adapter = budgetAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        budgetViewModel.allBudget.observe(viewLifecycleOwner, Observer { budget ->
            budget?.let { budgetAdapter.setData(it) }
        })

        budgetViewModel.budgetTransaction.observe(viewLifecycleOwner, Observer {
            it?.let { budgetAdapter.setBudgetTransaction(it) }
        })
        CoroutineScope(IO).launch {
            val date = SelectedDateSingleton.singletonSelectedDate
            budgetViewModel.getBudgetTransactionByDateLV(date.month, date.year)
        }

        val addBudget: Button = view.findViewById(R.id.button_add_budget)

        addBudget.setOnClickListener {
            val budget = Budget(budgetName = "New Budget", budgetGoal = 0.0, budgetAllocation = 0.0)
            CoroutineScope(IO).launch {
                val result = budgetViewModel.insertWithResult(budget)
                withContext(Main) {
                    if (result == -1L) {
//                        Toast.makeText(this@BudgetFragment.context, "Budget already exist", Toast.LENGTH_SHORT).show()
                        val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                        if (mainView != null) {
                            Snackbar.make(mainView, "Budget already exist", Snackbar.LENGTH_SHORT).show()
                        }
                    } else {
//                        Toast.makeText(this@BudgetFragment.context, "New budget inserted", Toast.LENGTH_SHORT).show()
                        val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                        if (mainView != null) {
                            Snackbar.make(mainView, "New budget inserted", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

//        (recyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false

        budgetAdapter.setOnItemClickListener(object : BudgetListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                budgetAdapter.flipIsExpanded(position)
            }

            override fun onPopupMenuButtonClick(view: View, budget: Budget) {
                val popupMenuButton: Button = view.findViewById(R.id.button_popup_menu)

                val popupMenu = PopupMenu(this@BudgetFragment.context!!, popupMenuButton)
                popupMenu.inflate(R.menu.menu_budget_option)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.popup_menu_edit_budget -> {
                            val intent = Intent(this@BudgetFragment.context, EditBudgetActivity::class.java)
                            intent.putExtra(EditBudgetActivity.EXTRA_BUDGET_ID, budget.budgetId)
                            startActivityForResult(intent, editBudgetCode)
                            true
                        }
                        R.id.popup_menu_delete_budget -> {
                            //confirm delete alert dialog
                            MaterialAlertDialogBuilder(this@BudgetFragment.context)
                                .setTitle("Confirm Delete")
                                .setMessage("Are you sure you want to delete this budget? This action cannot be undone.")
                                .setPositiveButton("Delete") { dialog, which ->
                                    //delete budget
                                    CoroutineScope(IO).launch {
                                        val result = budgetViewModel.delete(budget)
                                        if (result == 1) {
                                            withContext(Main) {
//                                                Snackbar.make(activity!!.findViewById<CoordinatorLayout>(R.id.budget_layout), "Budget Deleted", Snackbar.LENGTH_SHORT).show()
                                                val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                                                if (mainView != null) {
                                                    Snackbar.make(mainView, "Budget Deleted", Snackbar.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                }
                                .setNegativeButton("Cancel") { dialog, which ->
                                }
                                .show()
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        })

        val budgetingButton: Button = view.findViewById(R.id.button_budgeting)
        budgetingButton.setOnClickListener {
            val intent = Intent(this.context, BudgetingActivity::class.java)
            startActivityForResult(intent, editBudgetingCode)
        }

        return view
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == editBudgetCode && resultCode == Activity.RESULT_OK) {
            val result = data!!.getIntExtra(EditBudgetActivity.EXTRA_UPDATE_RESULT, -1)
            val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
            if (mainView != null) {
                if (result == 1) {
                    Snackbar.make(mainView, "Budget Save", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(mainView, "Error", Snackbar.LENGTH_SHORT).show()
                }
            }
        } else if (requestCode == editBudgetingCode && resultCode == Activity.RESULT_OK){
            CoroutineScope(IO).launch {
                Log.i("haha", "pls work")
                val date = SelectedDateSingleton.singletonSelectedDate
                budgetViewModel.getBudgetTransactionByDateLV(date.month, date.year)
            }
        }
    }

}
