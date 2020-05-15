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
import com.example.mfm_2.pojo.TransactionListAdapterDataObject
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.MFMViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*

private const val ARG_TRANSACTION = "com.example.mfm_2.fragment.transaction.ARG_TRANSACTION"

class TransferFragment : Fragment() {
    private var transaction: TransactionListAdapterDataObject? = null
    private lateinit var mfmViewModel: MFMViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")
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
            transaction = it.getParcelable(ARG_TRANSACTION)
            transaction?.let {transaction ->
                val accountFromDropdown: AutoCompleteTextView = view.findViewById(R.id.dropdown_from_account)
                val accountToDropdown: AutoCompleteTextView = view.findViewById(R.id.dropdown_to_account)
                val amountEditText: TextInputEditText = view.findViewById(R.id.textedit_transaction_amount)
                accountFromDropdown.setText(transaction.transactionAccountName)
                accountToDropdown.setText(transaction.transactionAccountTransferToName)
                amountEditText.setText(transaction.transactionAmount.toString())
            }

        }

        mfmViewModel.allAccount.observe(viewLifecycleOwner, Observer {
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
        val accountFrom = mfmViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountFromDropdown.text.toString() } }!!
        val accountTo = mfmViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountToDropdown.text.toString() } }!!
        val newTransaction = Transaction(
            transactionAccountId = accountFrom.accountId,
            transactionAccountTransferTo = accountTo.accountId,
            transactionType = "TRANSFER",
            transactionAmount = amount.text.toString().toDouble()
        )
        CoroutineScope(Dispatchers.IO).launch {
            mfmViewModel.insert(newTransaction)
        }
    }

    fun saveTransfer(transaction: Transaction) {
        val accountFromDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_from_account)
        val accountToDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_to_account)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val accountFrom =
            mfmViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountFromDropdown.text.toString() } }!!
        val accountTo = mfmViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountToDropdown.text.toString() } }!!
        transaction.transactionAmount = amount.text.toString().toDouble()
        transaction.transactionAccountId = accountFrom.accountId
        transaction.transactionAccountTransferTo = accountTo.accountId
        Log.i("haha", transaction.toString())
        CoroutineScope(Dispatchers.IO).launch {
            mfmViewModel.update(transaction)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(transactionListAdapterDataObject: TransactionListAdapterDataObject) =
            TransferFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TRANSACTION, transactionListAdapterDataObject)
                }
            }
    }
}
