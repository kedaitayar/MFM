package com.example.mfm_2.fragment.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTransactionActivity : AppCompatActivity() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var budgetViewModel: BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)
        setSupportActionBar(findViewById(R.id.toolbar_edit_transaction))
        title = "Edit Transaction"

        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        val buttonSave: Button = findViewById(R.id.button_save)
        val transactionId = intent.getLongExtra(EXTRA_TRANSACTION_ID, -1)

        val fragmentManager = supportFragmentManager.beginTransaction()
        val expenseFragment = ExpenseFragment.newInstance(transactionId)
        val incomeFragment = IncomeFragment.newInstance(transactionId)
        val transferFragment = TransferFragment.newInstance(transactionId)

        CoroutineScope(Dispatchers.IO).launch {
            var transaction = transactionViewModel.getTransactionById(transactionId)
            when (transaction.transactionType) {
                "EXPENSE" -> {
                    fragmentManager.replace(R.id.fragment_container, expenseFragment)
                    fragmentManager.commit()
                }
                "INCOME" -> {
                    fragmentManager.replace(R.id.fragment_container, incomeFragment)
                    fragmentManager.commit()

                }
                "TRANSFER" -> {
                    fragmentManager.replace(R.id.fragment_container, transferFragment)
                    fragmentManager.commit()
                }
                else -> {

                }
            }
        }

        buttonSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var transaction = transactionViewModel.getTransactionById(transactionId)
                withContext(Dispatchers.Main) {
                    when (transaction.transactionType) {
                        "EXPENSE" -> {
                            expenseFragment.saveExpense(transaction)
                            finish()
                        }
                        "INCOME" -> {
                            incomeFragment.saveIncome(transaction)
                            finish()
                        }
                        "TRANSFER" -> {
                            transferFragment.saveTransfer(transaction)
                            finish()
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_TRANSACTION_ID = "com.example.mfm_2.fragment.transaction.EXTRA_TRANSACTION_ID"
    }
}
