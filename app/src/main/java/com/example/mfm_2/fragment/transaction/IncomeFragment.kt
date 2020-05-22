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
import com.example.mfm_2.pojo.TransactionListAdapterDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.*

private const val ARG_TRANSACTION = "com.example.mfm_2.fragment.transaction.ARG_TRANSACTION"

class IncomeFragment : Fragment() {
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

        val view = inflater.inflate(R.layout.fragment_income2, container, false)
        val account: AutoCompleteTextView = view.findViewById(R.id.dropdown_account)
        val amount: TextInputEditText = view.findViewById(R.id.textedit_transaction_amount)

        arguments?.let {
            transaction = it.getParcelable(ARG_TRANSACTION)
            transaction?.let {transaction ->
                val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
                val amountEditText: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
                accountDropdown.setText(transaction.transactionAccountName)
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
                account.setAdapter(accountDropdownAdapter)
            }
        })

        return view
    }

    fun addIncome() {
        val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val account = mfmViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountDropdown.text.toString() } }!!
        val newTransaction =
            Transaction(transactionAccountId = account.accountId, transactionType = "INCOME", transactionAmount = amount.text.toString().toDouble())
        CoroutineScope(Dispatchers.IO).launch {
            mfmViewModel.insert(newTransaction)
        }
    }

//    fun setInitialData(transaction: Transaction) {
//        val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
//        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
//        CoroutineScope(Dispatchers.IO).launch {
//            val account = async { accountViewModel.getAccountById(transaction.transactionAccountId) }
//            withContext(Dispatchers.Main) {
//                accountDropdown.setText(account.await().accountName)
//                amount.setText(transaction.transactionAmount.toString())
//            }
//        }
//    }

    fun saveIncome(transaction: Transaction) {
        val accountDropdown: AutoCompleteTextView = view!!.findViewById(R.id.dropdown_account)
        val amount: TextInputEditText = view!!.findViewById(R.id.textedit_transaction_amount)
        val account = mfmViewModel.allAccount.value?.let { account -> account.find { it.accountName == accountDropdown.text.toString() } }!!
        transaction.transactionAmount = amount.text.toString().toDouble()
        transaction.transactionAccountId = account.accountId
        CoroutineScope(Dispatchers.IO).launch {
            mfmViewModel.update(transaction)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(transactionListAdapterDataObject: TransactionListAdapterDataObject) =
            IncomeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TRANSACTION, transactionListAdapterDataObject)
                }
            }
    }
}
