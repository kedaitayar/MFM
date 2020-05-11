package com.example.mfm_2.fragment.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mfm_2.R
import com.example.mfm_2.fragment.account.adapter.AccountListAdapter
import com.example.mfm_2.fragment.budget.edit.EditBudgetActivity
import com.example.mfm_2.model.Account
import com.example.mfm_2.viewmodel.AccountViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class AccountListFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    val editAccountCode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel = activity?.run { ViewModelProvider(this).get(AccountViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_account_list, container, false)
        val recyclerview: RecyclerView = view.findViewById(R.id.recyclerview_account_list)
        val recyclerViewAdapter = AccountListAdapter(this.context!!)
        recyclerview.adapter = recyclerViewAdapter
        recyclerview.layoutManager = GridLayoutManager(this.context, 2)
        accountViewModel.allAccount.observe(viewLifecycleOwner, Observer {
            it?.let { recyclerViewAdapter.setAccount(it) }
        })
        accountViewModel.allTransaction.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                val income = async { accountViewModel.getAccountIncome() }
                val expense = async { accountViewModel.getAccountExpense() }
                withContext(Dispatchers.Main) {
                    recyclerViewAdapter.setIncome(income.await())
                    recyclerViewAdapter.setExpense(expense.await())
                }

            }
        })
        recyclerViewAdapter.setOnItemClickListener(object : AccountListAdapter.OnItemClickListener {
            override fun onPopupMenuButtonClick(account: Account, popupMenuButton: Button) {
//                val popupMenuButton: Button = view.findViewById(R.id.button_popup_menu)
                val popupMenu = PopupMenu(this@AccountListFragment.context, popupMenuButton)
                popupMenu.inflate(R.menu.menu_all_account)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.popup_menu_add_fund_account -> {
//                            TODO("link to add transaction income")
                            true
                        }
                        R.id.popup_menu_edit_account -> {
                            val intent = Intent(this@AccountListFragment.context, EditAccountActivity::class.java)
                            intent.putExtra(EditAccountActivity.EXTRA_ACCOUNT_ID, account.accountId)
                            startActivityForResult(intent, editAccountCode)
                            true
                        }
                        R.id.popup_menu_delete_account -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                accountViewModel.delete(account)
                                withContext(Dispatchers.Main) {
                                    val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                                    if (mainView != null) {
                                        Snackbar.make(mainView, "Account deleted", Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                            }
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
        if (requestCode == editAccountCode && resultCode == Activity.RESULT_OK) {
            val result = data!!.getIntExtra(EditAccountActivity.EXTRA_UPDATE_RESULT, -1)
            val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
            if (mainView != null) {
                if (result == 1) {
                    Snackbar.make(mainView, "Budget Save", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(mainView, "Error", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
