package com.example.mfm_2.fragment.transaction

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.mfm_2.R
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*

private const val ARG_TRANSACTION_ID = "com.example.mfm_2.fragment.transaction.ARG_TRANSACTION_ID"


class ExpenseFragment : Fragment() {
    private var transactionId: Long? = null
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var budgetViewModel: BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = activity?.run { ViewModelProvider(this).get(TransactionViewModel::class.java) } ?: throw Exception("Invalid Activity")
        accountViewModel = activity?.run { ViewModelProvider(this).get(AccountViewModel::class.java) } ?: throw Exception("Invalid Activity")
        budgetViewModel = activity?.run { ViewModelProvider(this).get(BudgetViewModel::class.java) } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_expense2, container, false)
        val account: AutoCompleteTextView = view.findViewById(R.id.dropdown_account)
        val budget: AutoCompleteTextView = view.findViewById(R.id.dropdown_budget)
        val amount: TextInputEditText = view.findViewById(R.id.textedit_transaction_amount)

        arguments?.let {
            transactionId = it.getLong(ARG_TRANSACTION_ID)
            val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
            val budgetDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_budget)
            val amountEditText: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
            CoroutineScope(Dispatchers.IO).launch {
                val transaction = async { transactionViewModel.getTransactionById(transactionId!!) }
                val account = async { accountViewModel.getAccountById(transaction.await().transactionAccountId) }
                val budget = async { budgetViewModel.getBudgetById(transaction.await().transactionBudgetId!!) }
                withContext(Dispatchers.Main) {
                    accountDropdown.setText(account.await().accountName)
                    budgetDropdown.setText(budget.await().budgetName)
                    amountEditText.setText(transaction.await().transactionAmount.toString())
                }
            }
        }

        accountViewModel.allAccount.observe(viewLifecycleOwner, Observer {
            it?.let {
                var accountNameList: MutableList<String> = mutableListOf()
                for (account in it) {
                    accountNameList.add(account.accountName)
                }
                val accountDropdownAdapter = ArrayAdapter<String>(this.context!!, R.layout.dropdown_menu_popup_item, accountNameList)
                account.setAdapter(accountDropdownAdapter)
            }
        })
        budgetViewModel.allBudget.observe(viewLifecycleOwner, Observer {
            it?.let {
                var budgetNameList: MutableList<String> = mutableListOf()
                for (budget in it) {
                    budgetNameList.add(budget.budgetName)
                }
                val budgetDropdownAdapter = ArrayAdapter<String>(this.context!!, R.layout.dropdown_menu_popup_item, budgetNameList)
                budget.setAdapter(budgetDropdownAdapter)
            }
        })

        return view
    }

    fun addExpense() {
        val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
        val budgetDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_budget)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val account = accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountDropdown.text.toString() } }!!
        val budget = budgetViewModel.allBudget.value?.let { budget -> budget.find { it.budgetName == budgetDropdown.text.toString() } }!!
        val newTransaction = Transaction(
            transactionAccountId = account.accountId,
            transactionBudgetId = budget.budgetId,
            transactionType = "EXPENSE",
            transactionAmount = amount.text.toString().toDouble()
        )
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.insert(newTransaction)
        }
    }

    fun setInitialData(transaction: Transaction) {
        val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
        val budgetDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_budget)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        CoroutineScope(Dispatchers.IO).launch {
            val account = async { accountViewModel.getAccountById(transaction.transactionAccountId) }
            val budget = async { budgetViewModel.getBudgetById(transaction.transactionBudgetId!!) }
            withContext(Dispatchers.Main) {
                accountDropdown.setText(account.await().accountName)
                budgetDropdown.setText(budget.await().budgetName)
                amount.setText(transaction.transactionAmount.toString())
            }
        }
    }

    fun saveExpense(transaction: Transaction) {
        val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
        val budgetDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_budget)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val account = accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountDropdown.text.toString() } }!!
        val budget = budgetViewModel.allBudget.value?.let { budget -> budget.find { it.budgetName == budgetDropdown.text.toString() } }!!
        transaction.transactionAmount = amount.text.toString().toDouble()
        transaction.transactionAccountId = account.accountId
        transaction.transactionBudgetId = budget.budgetId
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.update(transaction)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(transactionId: Long) =
            ExpenseFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_TRANSACTION_ID, transactionId)
                }
            }
    }
}
