package com.example.mfm_2.fragment.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionListAdapterDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditTransactionActivity : AppCompatActivity() {
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)
        setSupportActionBar(findViewById(R.id.toolbar_edit_transaction))
        title = "Edit Transaction"

        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)

        val buttonSave: Button = findViewById(R.id.button_save)
        val transactionListAdapterDataObject = intent.getParcelableExtra<TransactionListAdapterDataObject>(EXTRA_TRANSACTION)

        val fragmentManager = supportFragmentManager.beginTransaction()
        val expenseFragment = ExpenseFragment.newInstance(transactionListAdapterDataObject)
        val incomeFragment = IncomeFragment.newInstance(transactionListAdapterDataObject)
        val transferFragment = TransferFragment.newInstance(transactionListAdapterDataObject)

        when (transactionListAdapterDataObject.transactionType) {
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
            else -> { }
        }

        buttonSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                var transaction = mfmViewModel.getTransactionById(transactionListAdapterDataObject.transactionId)
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
        const val EXTRA_TRANSACTION = "com.example.mfm_2.fragment.transaction.EXTRA_TRANSACTION"
    }
}
