package com.example.mfm_2.fragment.addEditTransaction

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider

import com.example.mfm_2.R
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class ExpenseFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private var listener: FragmentExpenseListener? = null

    interface FragmentExpenseListener {
        fun onExpenseInputSent(amount: Double, account: String, budget: String, id: Long)
        fun getEditData(): TransactionWithAccountBudget
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_expense, container, false)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        val editTextAmount: TextInputEditText = view.findViewById(R.id.text_input_amount)
        val editTextBudget: AutoCompleteTextView = view.findViewById(R.id.dropdown_budget)
        val editTextAccount: AutoCompleteTextView = view.findViewById(R.id.dropdown_account)
        val buttonSave: Button = view.findViewById(R.id.button_save)

        CoroutineScope(IO).launch {
            val accountName = accountViewModel.getAllAccountName()
            val accountAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this@ExpenseFragment.context!!,
                R.layout.dropdown_menu_popup_item,
                accountName
            )
            val accountDropdown: AutoCompleteTextView = view.findViewById(R.id.dropdown_account)

            val budgetName = budgetViewModel.getAllBudgetName()
            val budgetAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                this@ExpenseFragment.context!!,
                R.layout.dropdown_menu_popup_item,
                budgetName
            )
            val budgetDropdown: AutoCompleteTextView = view.findViewById(R.id.dropdown_budget)
            withContext(Main) {
                accountDropdown.setAdapter(accountAdapter)
                budgetDropdown.setAdapter(budgetAdapter)
            }
        }
        val editTransaction = listener!!.getEditData()
        if (editTransaction.transactionId > 0) {
            editTextAmount.setText(editTransaction.transactionAmount.toString())
            editTextAccount.setText(editTransaction.accountName)
            editTextBudget.setText(editTransaction.budgetName)
            buttonSave.text = "UPDATE"
        }

        buttonSave.setOnClickListener {
            val amount: Double = editTextAmount.text.toString().toDouble()
            val budget: String = editTextBudget.text.toString()
            val account: String = editTextAccount.text.toString()
            listener?.onExpenseInputSent(amount, account, budget, -1)
        }

        val textview: TextView = view.findViewById(R.id.textView)
        editTextAccount.setOnItemClickListener { adapterView, view, i, l ->
            CoroutineScope(IO).launch {
                val account = accountViewModel.getAccount(editTextAccount.text.toString())
                withContext(Main){
                    textview.visibility = TextView.GONE
                    textview.text = account.accountName + ": " + getString(R.string.currency) + account.accountBalance
                    textview.visibility = TextView.VISIBLE
                }
            }
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is FragmentExpenseListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement FragmentAListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

}
