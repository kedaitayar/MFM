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
import com.example.mfm_2.model.Budget
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
//    private val editBudgetCode = 1
//    private val editBudgetingCode = 2

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
//            startActivityForResult(intent, editBudgetingCode)
            startActivity(intent)
        }

        val budgetDetailButton: Button = view.findViewById(R.id.button_budget_detail)
        budgetDetailButton.setOnClickListener {
            val intent = Intent(this.context, BudgetDetailActivity::class.java)
            startActivity(intent)
        }

//        budgetAdapter.setOnItemClickListener(object : BudgetListAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
////                budgetAdapter.expandedOrCollapse(position)
//            }
//
//            override fun onPopupMenuButtonClick(view: View, budgetListAdapterDataObject: BudgetListAdapterDataObject) {
//                val popupMenuButton: Button = view.findViewById(R.id.button_popup_menu)
//
//                val popupMenu = PopupMenu(this@BudgetFragment.context!!, popupMenuButton)
//                popupMenu.inflate(R.menu.menu_budget_option)
//                popupMenu.setOnMenuItemClickListener {
//                    when (it.itemId) {
//                        R.id.popup_menu_edit_budget -> {
//                            val intent = Intent(this@BudgetFragment.context, EditBudgetActivity::class.java)
//                            intent.putExtra(EditBudgetActivity.EXTRA_BUDGET_ID, budgetListAdapterDataObject.budgetId)
//                            startActivityForResult(intent, editBudgetCode)
//                            true
//                        }
//                        R.id.popup_menu_delete_budget -> {
//                            //confirm delete alert dialog
//                            MaterialAlertDialogBuilder(this@BudgetFragment.context)
//                                .setTitle("Confirm Delete")
//                                .setMessage("Are you sure you want to delete this budget? This action cannot be undone.")
//                                .setPositiveButton("Delete") { dialog, which ->
//                                    //delete budget
//                                    CoroutineScope(IO).launch {
//                                        val budget = mfmViewModel.getBudgetById(budgetListAdapterDataObject.budgetId)
//                                        val result = mfmViewModel.delete(budget)
//                                        if (result == 1) {
//                                            withContext(Main) {
//                                                val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
//                                                if (mainView != null) {
//                                                    Snackbar.make(mainView, "Budget Deleted", Snackbar.LENGTH_SHORT).show()
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                                .setNegativeButton("Cancel") { dialog, which ->
//                                }
//                                .show()
//                            true
//                        }
//                        else -> false
//                    }
//                }
//                popupMenu.show()
//            }
//        })

        val info: Button = view.findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Budget")
                .setMessage("TODO")
                .setPositiveButton("OK"){ dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }
//        TODO("info_butto x 9")

        return view
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == editBudgetCode && resultCode == Activity.RESULT_OK) {
//            val result = data!!.getIntExtra(EditBudgetActivity.EXTRA_UPDATE_RESULT, -1)
//            val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
//            if (mainView != null) {
//                if (result == 1) {
//                    Snackbar.make(mainView, "Budget Save", Snackbar.LENGTH_SHORT).show()
//                } else {
//                    Snackbar.make(mainView, "Error", Snackbar.LENGTH_SHORT).show()
//                }
//            }
//        } else if (requestCode == editBudgetingCode && resultCode == Activity.RESULT_OK) {
////            CoroutineScope(IO).launch {
////                val date = SelectedDateSingleton.singletonSelectedDate
////                budgetViewModel.getBudgetTransactionByDateLV(date.month, date.year)
////            }
//        }
//    }

}
