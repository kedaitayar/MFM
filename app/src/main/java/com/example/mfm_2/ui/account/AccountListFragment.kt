package com.example.mfm_2.ui.account

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.mfm_2.R
import com.example.mfm_2.ui.account.adapter.AccountListAdapter
import com.example.mfm_2.util.pojo.AccountListAdapterDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class AccountListFragment : Fragment() {
    private lateinit var mfmViewModel: MFMViewModel
    val editAccountCode = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.fragment_account_list, container, false)
        val recyclerview: RecyclerView = view.findViewById(R.id.recyclerview_account_list)
        val recyclerViewAdapter = AccountListAdapter(this.context!!)
        recyclerview.adapter = recyclerViewAdapter
        recyclerview.layoutManager = GridLayoutManager(this.context, 2)

        mfmViewModel.accountListData.observe(viewLifecycleOwner, Observer {
            it?.let { recyclerViewAdapter.submitList(it) }
        })

        recyclerViewAdapter.setOnItemClickListener(object : AccountListAdapter.OnItemClickListener {
            override fun onPopupMenuButtonClick(accountListAdapterDataObject: AccountListAdapterDataObject, popupMenuButton: Button) {
                val popupMenu = PopupMenu(this@AccountListFragment.context, popupMenuButton)
                popupMenu.inflate(R.menu.menu_all_account)
                popupMenu.setOnMenuItemClickListener {
                    when (it.itemId) {
//                        R.id.popup_menu_add_fund_account -> {
//                            TODO("link to add transaction income")
//                            true
//                        }
                        R.id.popup_menu_edit_account -> {
                            val intent = Intent(this@AccountListFragment.context, EditAccountActivity::class.java)
                            intent.putExtra(EditAccountActivity.EXTRA_ACCOUNT_ID, accountListAdapterDataObject.accountId)
                            startActivityForResult(intent, editAccountCode)
                            true
                        }
                        R.id.popup_menu_delete_account -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                val account = mfmViewModel.getAccountById(accountListAdapterDataObject.accountId)
                                mfmViewModel.delete(account)
                                withContext(Dispatchers.Main) {
                                    val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
                                    if (mainView != null) {
                                        Snackbar.make(mainView, "Account deleted", Snackbar.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }

            override fun onItemClick(accountId: Long) {
                mfmViewModel.setSelectedAccount(accountId)
            }
        })

        val info: Button = view.findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Account")
                .setMessage("This card display all account. It also show the current balance of each account.\nTo edit or delete account, click the overflow menu(3 dot) and click edit or delete.")
                .setPositiveButton("OK"){ dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == editAccountCode && resultCode == Activity.RESULT_OK) {
            val result = data!!.getIntExtra(EditAccountActivity.EXTRA_UPDATE_RESULT, -1)
            val mainView: CoordinatorLayout? = activity?.findViewById(R.id.main_coordinator_layout)
            if (mainView != null) {
                if (result == 1) {
                    Snackbar.make(mainView, "Account Save", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(mainView, "Error. Account name cannot be same to existing account", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
