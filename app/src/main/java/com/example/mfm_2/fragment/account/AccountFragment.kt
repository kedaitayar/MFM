package com.example.mfm_2.fragment.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.fragment.budget.edit.EditBudgetActivity
import com.example.mfm_2.model.Account
import com.example.mfm_2.viewmodel.AccountViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel = activity?.run { ViewModelProvider(this).get(AccountViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_acccount, container, false)
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().apply {
            replace(R.id.fragment_account_container1, AccountListFragment())
        }
        fragmentTransaction.commit()

        val buttonAddAccount: Button = view.findViewById(R.id.button_add_account)
        buttonAddAccount.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val newAccount = Account(accountName = "New Account")
                accountViewModel.insertWithResult(newAccount)
                withContext(Dispatchers.Main) {
                    val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                    if (mainView != null) {
                        Snackbar.make(mainView, "New account inserted", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        return view
    }
}
