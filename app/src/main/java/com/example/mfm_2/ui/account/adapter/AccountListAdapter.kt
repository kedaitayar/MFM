package com.example.mfm_2.ui.account.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.pojo.AccountListAdapterDataObject
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class AccountListAdapter internal constructor(context: Context) :
    ListAdapter<AccountListAdapterDataObject, AccountListAdapter.ViewHolder>(AccountListDiffCallback()) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onPopupMenuButtonClick(accountListAdapterDataObject: AccountListAdapterDataObject, popupMenuButton: Button)
        fun onItemClick(accountId: Long)
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
            v.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(getItem(position).accountId)
                }
            }
        }

        val accountName: TextView = v.findViewById(R.id.textView4)
        val accountBalance: TextView = v.findViewById(R.id.textView5)
        val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_account_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        val resources: Resources = holder.itemView.context.resources
        val formatter = DecimalFormat(resources.getString(R.string.currency_format))
        val format = DecimalFormatSymbols(Locale.getDefault())
        format.groupingSeparator = ' '
        formatter.decimalFormatSymbols = format
        holder.accountName.text = current.accountName
        val balance = (current.accountIncome - current.accountExpense - current.accountTransferOut + current.accountTransferIn)
        holder.accountBalance.text = resources.getString(R.string.currency, formatter.format(balance))
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