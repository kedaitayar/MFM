package com.example.mfm_2.fragment.transaction

import android.os.Bundle
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class IncomeFragment : Fragment() {
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

        val view = inflater.inflate(R.layout.fragment_income2, container, false)
        val account: AutoCompleteTextView = view.findViewById(R.id.dropdown_account)
        val amount: TextInputEditText = view.findViewById(R.id.textedit_transaction_amount)

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

        return view
    }

    fun addIncome(){
        val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val account = accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountDropdown.text.toString() } }!!
        val newTransaction = Transaction(transactionAccountId = account.accountId, transactionType = "INCOME", transactionAmount = amount.text.toString().toDouble())
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.insert(newTransaction)
        }
    }
}
