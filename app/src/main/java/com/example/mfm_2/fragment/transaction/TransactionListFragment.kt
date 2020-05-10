package com.example.mfm_2.fragment.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mfm_2.R
import com.example.mfm_2.fragment.transaction.adapter.TransactionListAdapter
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel

/**
 * A simple [Fragment] subclass.
 */
class TransactionListFragment : Fragment() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var budgetViewModel: BudgetViewModel

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
            it?.let { recyclerViewAdapter.setTransaction(it) }
        })
        accountViewModel.allAccount.observe(viewLifecycleOwner, Observer {
            it?.let { recyclerViewAdapter.setAccount(it) }
        })
        budgetViewModel.allBudget.observe(viewLifecycleOwner, Observer {
            it?.let { recyclerViewAdapter.setBudget(it) }
        })

        return view
    }

}
