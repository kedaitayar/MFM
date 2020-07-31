package com.example.mfm_2.ui.transaction

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.ui.transaction.adapter.TransactionListAdapter
import com.example.mfm_2.util.pojo.TransactionListAdapterDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_TRANSACTION_TYPE = "com.example.mfm_2.ui.transaction.ARG_TRANSACTION_TYPE"

class TransactionListFragment : Fragment() {
    private lateinit var mfmViewModel: MFMViewModel
    val editTransactionCode = 1
    private var transactionType: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")
        arguments?.let {
            transactionType = it.getString(ARG_TRANSACTION_TYPE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_transaction_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_transaction_list)
        val recyclerViewAdapter = TransactionListAdapter(this.context!!)
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)

        val data = when (transactionType) {
            TRANSACTION_TYPE_ACCOUNT_LIST -> {
                mfmViewModel.accountTransactionList
            }
            else -> {
                mfmViewModel.transactionListData
            }
        }

        data.observe(viewLifecycleOwner, Observer {
            it?.let {
                recyclerViewAdapter.submitList(it)
            }
        })

        recyclerViewAdapter.setOnItemClickListener(object : TransactionListAdapter.OnItemClickListener {
            override fun onPopupMenuButtonClick(transactionList: TransactionListAdapterDataObject, popupMenuButton: Button) {
                val popupMenu = PopupMenu(this@TransactionListFragment.context, popupMenuButton)
                popupMenu.inflate(R.menu.menu_transaction_list)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.popup_menu_edit_transaction -> {
                            val intent = Intent(this@TransactionListFragment.context, EditTransactionActivity::class.java)
                            intent.putExtra(EditTransactionActivity.EXTRA_TRANSACTION, transactionList)
                            startActivityForResult(intent, editTransactionCode)
                            true
                        }
                        R.id.popup_menu_delete_transaction -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                val transaction = mfmViewModel.getTransactionById(transactionList.transactionId)
                                val result = mfmViewModel.delete(transaction)
                                if (result == 1) {
                                    withContext(Dispatchers.Main) {
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

    companion object {
        @JvmStatic
        fun newInstance(transactionType: String) =
            TransactionListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TRANSACTION_TYPE, transactionType)
                }
            }

        const val TRANSACTION_TYPE_ACCOUNT_LIST = "com.example.mfm_2.ui.transaction.TRANSACTION_TYPE_ACCOUNT_LIST"
    }
}
