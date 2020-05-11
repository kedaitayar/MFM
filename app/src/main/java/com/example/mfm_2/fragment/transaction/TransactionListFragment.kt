package com.example.mfm_2.fragment.transaction

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mfm_2.R
import com.example.mfm_2.fragment.transaction.adapter.TransactionListAdapter
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class TransactionListFragment : Fragment() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    val editTransactionCode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel = activity?.run { ViewModelProvider(this).get(TransactionViewModel::class.java) } ?: throw Exception("Invalid Activity")
        accountViewModel = activity?.run { ViewModelProvider(this).get(AccountViewModel::class.java) } ?: throw Exception("Invalid Activity")
        budgetViewModel = activity?.run { ViewModelProvider(this).get(BudgetViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_transaction_list)
        val recyclerViewAdapter = TransactionListAdapter(this.context!!)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        transactionViewModel.allTransaction.observe(viewLifecycleOwner, Observer {
            it?.let {
                recyclerViewAdapter.setTransaction(it)
            }
        })
        accountViewModel.allAccount.observe(viewLifecycleOwner, Observer {
            it?.let { recyclerViewAdapter.setAccount(it) }
        })
        budgetViewModel.allBudget.observe(viewLifecycleOwner, Observer {
            it?.let { recyclerViewAdapter.setBudget(it) }
        })

        recyclerViewAdapter.setOnItemClickListener(object : TransactionListAdapter.OnItemClickListener {
            override fun onPopupMenuButtonClick(transaction: Transaction, popupMenuButton: Button) {
                val popupMenu = PopupMenu(this@TransactionListFragment.context, popupMenuButton)
                popupMenu.inflate(R.menu.menu_transaction_list)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.popup_menu_edit_transaction -> {
                            val intent = Intent(this@TransactionListFragment.context, EditTransactionActivity::class.java)
                            intent.putExtra(EditTransactionActivity.EXTRA_TRANSACTION_ID, transaction.transactionId)
                            startActivityForResult(intent, editTransactionCode)
                            true
                        }
                        R.id.popup_menu_delete_transaction -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                val result = transactionViewModel.delete(transaction)
                                if (result == 1) {
                                    withContext(Dispatchers.Main){
                                        val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                                        if (mainView != null) {
                                            Snackbar.make(mainView, "Transaction deleted", Snackbar.LENGTH_SHORT).show()
                                        }
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

}
