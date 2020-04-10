package com.example.mfm_2.fragment.addEditTransaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddEditTransactionActivity : AppCompatActivity(), ExpenseFragment.FragmentExpenseListener {
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var budgetViewModel: BudgetViewModel
    private lateinit var transactionViewModel: TransactionViewModel
    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {item ->
        when(item.itemId){
            R.id.nav_transfer -> {
                val fragment = TransferFragment()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_expense -> {
                val fragment = ExpenseFragment()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.nav_income -> {
                val fragment = IncomeFragment()
                openFragment(fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_transaction)
        setSupportActionBar(findViewById(R.id.toolbar))
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        val bottomNav = findViewById<BottomNavigationView>(R.id.transaction_menu)
        bottomNav.setOnNavigationItemSelectedListener(navListener)
//        bottomNav.selectedItemId = R.id.nav_expense

        val transactionId: Long = intent.getLongExtra(EXTRA_TRANSACTION_ID, -1)
        if (transactionId > 0){
//            title = "Edit Transaction"
            title = intent.getStringExtra(EXTRA_AMOUNT)
            if (intent.getStringExtra(EXTRA_TYPE) == EXPENSE) {
                bottomNav.selectedItemId = R.id.nav_expense
            } else if (intent.getStringExtra(EXTRA_TYPE) == INCOME) {
                bottomNav.selectedItemId = R.id.nav_income
            }
        } else {
            title = "Add Transaction"
            bottomNav.selectedItemId = R.id.nav_expense
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.addedit_transaction_fragment_frame, fragment, fragment.javaClass.simpleName)
            .commit()
    }

    override fun onExpenseInputSent(amount: Double, account: String, budget: String, id: Long) {
        CoroutineScope(IO).launch {
            val accountId: Long = accountViewModel.getAccount(account).accountId
            val budgetId: Long = budgetViewModel.getBudget(budget).budgetId
            val newTransaction: Transaction = Transaction(transactionAmount = amount, transactionAccountId = accountId, transactionBudgetId = budgetId, transactionType = "EXPENSE")
            transactionViewModel.insert(newTransaction)
        }
        finish()
    }

    override fun getEditData(): TransactionWithAccountBudget {
        return if (intent.hasExtra(EXTRA_TRANSACTION_ID)) {
            TransactionWithAccountBudget(
                transactionId = intent.getLongExtra(EXTRA_TRANSACTION_ID, -1),
                transactionAmount = intent.getDoubleExtra(EXTRA_AMOUNT, -1.0),
                accountName = intent.getStringExtra(EXTRA_ACCOUNT),
                budgetName = intent.getStringExtra(EXTRA_BUDGET),
                transactionType = intent.getStringExtra(EXTRA_TYPE)
            )
        } else {
            TransactionWithAccountBudget()
        }
    }

    companion object{
        const val EXTRA_TRANSACTION_ID = "com.example.mfm_2.TRANSACTION_ID"
        const val EXTRA_AMOUNT = "com.example.mfm_2.AMOUNT"
        const val EXTRA_ACCOUNT = "com.example.mfm_2.ACCOUNT"
        const val EXTRA_BUDGET = "com.example.mfm_2.BUDGET"
        const val EXTRA_TYPE = "com.example.mfm_2.TYPE"
        const val EXPENSE = "EXPENSE"
        const val INCOME = "INCOME"
    }
}
