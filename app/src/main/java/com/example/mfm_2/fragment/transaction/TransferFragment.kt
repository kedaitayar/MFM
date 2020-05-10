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
import com.example.mfm_2.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class TransferFragment : Fragment() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        transactionViewModel = activity?.run { ViewModelProvider(this).get(TransactionViewModel::class.java) } ?: throw Exception("Invalid Activity")
        accountViewModel = activity?.run { ViewModelProvider(this).get(AccountViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_transfer2, container, false)
        val accountFrom: AutoCompleteTextView = view.findViewById(R.id.dropdown_from_account)
        val accountTo: AutoCompleteTextView = view.findViewById(R.id.dropdown_to_account)
        val amount: TextInputEditText = view.findViewById(R.id.textedit_transaction_amount)

        accountViewModel.allAccount.observe(viewLifecycleOwner, Observer {
            it?.let {
                var accountNameList: MutableList<String> = mutableListOf()
                for (account in it) {
                    accountNameList.add(account.accountName)
                }
                val accountDropdownAdapter = ArrayAdapter<String>(this.context!!, R.layout.dropdown_menu_popup_item, accountNameList)
                accountFrom.setAdapter(accountDropdownAdapter)
                accountTo.setAdapter(accountDropdownAdapter)
            }
        })

        return view
    }

    fun addTransfer() {
        val accountFromDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_from_account)
        val accountToDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_to_account)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val accountFrom = accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountFromDropdown.text.toString() } }!!
        val accountTo = accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountToDropdown.text.toString() } }!!
        val newTransaction = Transaction(transactionAccountId = accountFrom.accountId, transactionAccountTransferTo = accountTo.accountId, transactionType = "TRANSFER", transactionAmount = amount.text.toString().toDouble())
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.insert(newTransaction)
        }
    }
}
