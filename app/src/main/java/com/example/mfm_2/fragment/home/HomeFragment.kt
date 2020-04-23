package com.example.mfm_2.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.fragment.account.NotBudgetedFragment
import com.example.mfm_2.fragment.addEditTransaction.AddEditTransactionActivity
import com.example.mfm_2.fragment.home.adapter.AccountListAdapter
import com.example.mfm_2.fragment.home.adapter.TransactionListAdapter
import com.example.mfm_2.model.Account
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var transactionViewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().apply {
//            replace(R.id.fragment_container1, NotBudgetedFragment())
            replace(R.id.fragment_container1, NotBudgetedFragment())
        }
        fragmentTransaction.commit()

        // Account recyclerview
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview)
        val adapter = AccountListAdapter(this.context!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        accountViewModel.allAccount.observe(viewLifecycleOwner, Observer { data ->
            // Update the cached copy of the words in the adapter.
            data?.let { adapter.setData(it) }
        })

        adapter.setOnItemClickListener(object : AccountListAdapter.OnItemClickListener{
            override fun onItemClick(account: Account) {
                Toast.makeText(this@HomeFragment.context , account.accountName, Toast.LENGTH_SHORT).show()
//                TODO("implement edit account")
            }
        })

        // Transaction recyclerview
        val recyclerView2: RecyclerView = view.findViewById(R.id.recyclerview2)
        val adapter2 = TransactionListAdapter(this.context!!)
        recyclerView2.adapter = adapter2
        recyclerView2.layoutManager = LinearLayoutManager(this.context)
        transactionViewModel.allTransaction2.observe(viewLifecycleOwner, Observer { transaction ->
            transaction?.let { adapter2.setData(it) }
        })

        adapter2.setOnItemClickListener(object : TransactionListAdapter.OnItemClickListener {
            override fun onItemClick(transaction: TransactionWithAccountBudget) {
                val intent = Intent(this@HomeFragment.context, AddEditTransactionActivity::class.java)
                intent.putExtra(AddEditTransactionActivity.EXTRA_TRANSACTION_ID, transaction.transactionId)
                intent.putExtra(AddEditTransactionActivity.EXTRA_AMOUNT, transaction.transactionAmount)
                intent.putExtra(AddEditTransactionActivity.EXTRA_ACCOUNT, transaction.accountName)
                intent.putExtra(AddEditTransactionActivity.EXTRA_BUDGET, transaction.budgetName)
                intent.putExtra(AddEditTransactionActivity.EXTRA_TYPE, transaction.transactionType)

                startActivity(intent)
            }
        })

        return view
    }
}
