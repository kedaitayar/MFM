package com.example.mfm_2.ui.budget

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.PopupMenu
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.ui.budget.adapter.BudgetListAdapter
import com.example.mfm_2.ui.budget.edit.EditBudgetActivity
import com.example.mfm_2.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_BUDGET_TYPE = "budgetType"

/**
 * A simple [Fragment] subclass.
 * Use the [BudgetListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BudgetListFragment : Fragment() {
    private var budgetType: Int? = null
    private lateinit var mfmViewModel: MFMViewModel
    private val editBudgetCode = 1
//    private val editBudgetingCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")
        arguments?.let {
            budgetType = it.getInt(ARG_BUDGET_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_budget)
//        val budgetAdapter = BudgetListAdapter()
        val budgetAdapter = BudgetListAdapter()
        recyclerView.adapter = budgetAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val data = when (budgetType) {
            1 -> {
                mfmViewModel.monthlyBudgetListData
            }
            2 -> {
                mfmViewModel.yearlyBudgetListData
            }
            3 -> {
                mfmViewModel.debtBudgetListData
            }
            else -> {
                mfmViewModel.budgetListData
            }
        }

        data.observe(viewLifecycleOwner, Observer {
            it?.let {
                budgetAdapter.submitData(it)
            }
        })
        mfmViewModel.allBudgetDeadline.observe(viewLifecycleOwner, Observer {
            it?.let {
                budgetAdapter.submitDeadlineData(it)
            }
        })
        mfmViewModel.selectedDate.observe(viewLifecycleOwner, Observer {
            it?.let {
                budgetAdapter.submitSelectedDate(it)
            }
        })

        budgetAdapter.setOnItemClickListener(object : BudgetListAdapter.OnItemClickListener {
//            override fun onItemClick(position: Int) {
////                budgetAdapter.expandedOrCollapse(position)
//            }

            override fun onPopupMenuButtonClick(view: View, budgetListAdapterDataObject: BudgetListAdapterDataObject) {
                val popupMenuButton: Button = view.findViewById(R.id.button_popup_menu)

                val popupMenu = PopupMenu(this@BudgetListFragment.context!!, popupMenuButton)
                popupMenu.inflate(R.menu.menu_budget_option)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.popup_menu_edit_budget -> {
                            val intent = Intent(this@BudgetListFragment.context, EditBudgetActivity::class.java)
                            intent.putExtra(EditBudgetActivity.EXTRA_BUDGET_ID, budgetListAdapterDataObject.budgetId)
                            startActivityForResult(intent, editBudgetCode)
                            true
                        }
                        R.id.popup_menu_delete_budget -> {
                            //confirm delete alert dialog
                            MaterialAlertDialogBuilder(this@BudgetListFragment.context)
                                .setTitle("Confirm Delete")
                                .setMessage("Are you sure you want to delete this budget? This action cannot be undone.")
                                .setPositiveButton("Delete") { dialog, which ->
                                    //delete budget
                                    CoroutineScope(Dispatchers.IO).launch {
                                        val budget = mfmViewModel.getBudgetById(budgetListAdapterDataObject.budgetId)
                                        val result = mfmViewModel.delete(budget)
                                        if (result == 1) {
                                            withContext(Dispatchers.Main) {
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

        return view
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
        }
//        else if (requestCode == editBudgetingCode && resultCode == Activity.RESULT_OK) {
//            CoroutineScope(IO).launch {
//                val date = SelectedDateSingleton.singletonSelectedDate
//                budgetViewModel.getBudgetTransactionByDateLV(date.month, date.year)
//            }
//        }
    }

    companion object {
        @JvmStatic
        fun newInstance(budgetType: Int) =
            BudgetListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_BUDGET_TYPE, budgetType)
                }
            }
    }
}