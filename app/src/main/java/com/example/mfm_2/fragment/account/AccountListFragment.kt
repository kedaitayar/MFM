package com.example.mfm_2.fragment.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mfm_2.R
import com.example.mfm_2.fragment.account.adapter.AccountListAdapter
import com.example.mfm_2.model.Account
import com.example.mfm_2.viewmodel.AccountViewModel

/**
 * A simple [Fragment] subclass.
 */
class AccountListFragment : Fragment() {
    private lateinit var accountViewModel: AccountViewModel
    private val editAccountCode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        accountViewModel = activity?.run { ViewModelProvider(this).get(AccountViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_account_list, container, false)
        val recyclerview: RecyclerView = view.findViewById(R.id.recyclerview_account_list)
        val recyclerViewAdapter = AccountListAdapter(this.context!!)
        recyclerview.adapter = recyclerViewAdapter
        recyclerview.layoutManager = GridLayoutManager(this.context, 2)
        accountViewModel.allAccount.observe(viewLifecycleOwner, Observer {
            it?.let { recyclerViewAdapter.setAccount(it) }
        })
        recyclerViewAdapter.setOnItemClickListener(object : AccountListAdapter.OnItemClickListener {
            override fun onPopupMenuButtonClick(account: Account) {
                val popupMenuButton: Button = view.findViewById(R.id.button_popup_menu)
                val popupMenu = PopupMenu(this@AccountListFragment.context, popupMenuButton)
                popupMenu.inflate(R.menu.menu_all_account)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.popup_menu_edit_account -> {
                            val intent = Intent(this@AccountListFragment.context, EditAccountActivity::class.java)
                            intent.putExtra(EditAccountActivity.EXTRA_ACCOUNT_ID, account.accountId)
                            startActivityForResult(intent, editAccountCode)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }

        })


        return view
    }

}
