package com.example.mfm_2.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.entity.Account
import com.example.mfm_2.ui.transaction.TransactionListFragment
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_acccount, container, false)
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().apply {
            replace(R.id.fragment_account_container1, AccountListFragment())
            replace(R.id.fragment_account_container2, AccountGraphFragment())
            replace(R.id.fragment_account_container3, TransactionListFragment.newInstance(TransactionListFragment.TRANSACTION_TYPE_ACCOUNT_LIST))
        }
        fragmentTransaction.commit()

        val buttonAddAccount: Button = view.findViewById(R.id.button_add_account)
        buttonAddAccount.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val newAccount = Account(accountName = "New Account")
                mfmViewModel.insert(newAccount)
                withContext(Dispatchers.Main) {
                    val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                    if (mainView != null) {
                        Snackbar.make(mainView, "New account inserted", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val info: Button = view.findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Account")
                .setMessage("TODO")
                .setPositiveButton("OK"){ dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }

        return view
    }
}
