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
import com.example.mfm_2.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*

private const val ARG_TRANSACTION_ID = "com.example.mfm_2.fragment.transaction.ARG_TRANSACTION_ID"

class TransferFragment : Fragment() {
    private var transactionId: Long? = null
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionViewModel = activity?.run { ViewModelProvider(this).get(TransactionViewModel::class.java) } ?: throw Exception("Invalid Activity")
        accountViewModel = activity?.run { ViewModelProvider(this).get(AccountViewModel::class.java) } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_transfer2, container, false)
        val accountFrom: AutoCompleteTextView = view.findViewById(R.id.dropdown_from_account)
        val accountTo: AutoCompleteTextView = view.findViewById(R.id.dropdown_to_account)
        val amount: TextInputEditText = view.findViewById(R.id.textedit_transaction_amount)

        arguments?.let {
            transactionId = it.getLong(ARG_TRANSACTION_ID)
            val accountFromDropdown: AutoCompleteTextView = view.findViewById(R.id.dropdown_from_account)
            val accountToDropdown: AutoCompleteTextView = view.findViewById(R.id.dropdown_to_account)
            val amountEditText: TextInputEditText = view.findViewById(R.id.textedit_transaction_amount)
            CoroutineScope(Dispatchers.IO).launch {
                val transaction = async { transactionViewModel.getTransactionById(transactionId!!) }
                val accountFrom = async { accountViewModel.getAccountById(transaction.await().transactionAccountId) }
                val accountTo = async { accountViewModel.getAccountById(transaction.await().transactionAccountTransferTo) }
                withContext(Dispatchers.Main) {
                    accountFromDropdown.setText(accountFrom.await().accountName)
                    accountToDropdown.setText(accountTo.await().accountName)
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
        val accountFrom =
            accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountFromDropdown.text.toString() } }!!
        val accountTo = accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountToDropdown.text.toString() } }!!
        val newTransaction = Transaction(
            transactionAccountId = accountFrom.accountId,
            transactionAccountTransferTo = accountTo.accountId,
            transactionType = "TRANSFER",
            transactionAmount = amount.text.toString().toDouble()
        )
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.insert(newTransaction)
        }
    }

    fun saveTransfer(transaction: Transaction) {
        val accountFromDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_from_account)
        val accountToDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_to_account)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val accountFrom =
            accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountFromDropdown.text.toString() } }!!
        val accountTo = accountViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountToDropdown.text.toString() } }!!
        transaction.transactionAmount = amount.text.toString().toDouble()
        transaction.transactionAccountId = accountFrom.accountId
        transaction.transactionAccountTransferTo = accountTo.accountId
        Log.i("haha", transaction.toString())
        CoroutineScope(Dispatchers.IO).launch {
            transactionViewModel.update(transaction)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(transactionId: Long) =
            TransferFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_TRANSACTION_ID, transactionId)
                }
            }
    }
}
