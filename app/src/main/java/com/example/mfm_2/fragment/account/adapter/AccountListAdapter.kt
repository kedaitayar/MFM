package com.example.mfm_2.fragment.account.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Account
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.AccountListAdapterDataObject

class AccountListAdapter internal constructor(context: Context) : ListAdapter<AccountListAdapterDataObject, AccountListAdapter.ViewHolder>(AccountListDiffCallback()) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: OnItemClickListener? = null
//    private var account = emptyList<Account>()
//    private var accountIncome = emptyList<Transaction>()
//    private var accountExpense = emptyList<Transaction>()
//    private var accountTransfer = emptyList<Transaction>()
//    private var accountBalance = emptyList<Transaction>()
//    private var accountList = emptyList<AccountListAdapterDataObject>()

    interface OnItemClickListener {
        fun onPopupMenuButtonClick(accountListAdapterDataObject: AccountListAdapterDataObject, popupMenuButton: Button)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
            popupMenuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onPopupMenuButtonClick(getItem(position), popupMenuButton)
                }
            }
        }

        val accountName: TextView = v.findViewById(R.id.textView4)
        val accountBalance: TextView = v.findViewById(R.id.textView5)
        val accountAllocationBalance: TextView = v.findViewById(R.id.textView7)
        val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_account_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.accountName.text = current.accountName
        holder.accountBalance.text = (current.accountIncome - current.accountExpense - current.accountTransferOut + current.accountTransferIn).toString()
        holder.accountAllocationBalance.text = "TODO"
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private class AccountListDiffCallback : DiffUtil.ItemCallback<AccountListAdapterDataObject>() {
        override fun areItemsTheSame(oldItem: AccountListAdapterDataObject, newItem: AccountListAdapterDataObject): Boolean {
            return (oldItem.accountId == newItem.accountId)
        }

        override fun areContentsTheSame(oldItem: AccountListAdapterDataObject, newItem: AccountListAdapterDataObject): Boolean {
            return ((oldItem.accountName == newItem.accountName) && (oldItem.accountIncome == newItem.accountIncome) && (oldItem.accountExpense == newItem.accountExpense) && (oldItem.accountTransferIn == newItem.accountTransferIn) && (oldItem.accountTransferOut == newItem.accountTransferOut))
        }

    }
}