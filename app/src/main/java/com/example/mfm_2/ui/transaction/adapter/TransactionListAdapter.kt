package com.example.mfm_2.ui.transaction.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.pojo.TransactionListAdapterDataObject
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class TransactionListAdapter internal constructor(context: Context) :
    ListAdapter<TransactionListAdapterDataObject, TransactionListAdapter.ViewHolder>(TransactionListDiffCallback()) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onPopupMenuButtonClick(transactionList: TransactionListAdapterDataObject, popupMenuButton: Button)
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

        val transactionAmount: TextView = v.findViewById(R.id.textView_transaction_amount)
        val transactionDate: TextView = v.findViewById(R.id.textView_date)
        val transactionBudget: TextView = v.findViewById(R.id.textView_budget)
        val transactionAccount: TextView = v.findViewById(R.id.textView_account)
        val transactionAccountTo: TextView = v.findViewById(R.id.textView_account_To)
        val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_transaction_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTransaction = getItem(position)
        val resources: Resources = holder.itemView.context.resources
        val formatter = DecimalFormat(resources.getString(R.string.currency_format))
        val format = DecimalFormatSymbols(Locale.getDefault())
        format.groupingSeparator = ' '
        formatter.decimalFormatSymbols = format
        when (currentTransaction.transactionType) {
            "EXPENSE" -> {
                holder.transactionAccount.text = currentTransaction.transactionAccountName
                holder.transactionBudget.text = currentTransaction.transactionBudgetName
                holder.transactionAccountTo.visibility = View.GONE
                holder.transactionAmount.setTextColor(Color.parseColor("#d50000"))
            }
            "INCOME" -> {
                holder.transactionAccount.text = currentTransaction.transactionAccountName
                holder.transactionAccountTo.visibility = View.GONE
                holder.transactionBudget.text = "Income"
                holder.transactionAmount.setTextColor(Color.parseColor("#00c853"))
            }
            "TRANSFER" -> {
                holder.transactionAccountTo.text = currentTransaction.transactionAccountTransferToName
                holder.transactionAccountTo.visibility = View.VISIBLE
                holder.transactionAccount.text = currentTransaction.transactionAccountName
                holder.transactionBudget.text = "Transfer"
                holder.transactionAmount.setTextColor(Color.parseColor("#000000"))
            }
        }
        holder.transactionAmount.text = resources.getString(R.string.currency, formatter.format(currentTransaction.transactionAmount))
        holder.transactionDate.text = resources.getString(
            R.string.date,
            currentTransaction.transactionTime.get(Calendar.DAY_OF_MONTH),
            currentTransaction.transactionTime.get(Calendar.MONTH) + 1,
            currentTransaction.transactionTime.get(Calendar.YEAR)
        )
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private class TransactionListDiffCallback : DiffUtil.ItemCallback<TransactionListAdapterDataObject>() {
        override fun areItemsTheSame(oldItem: TransactionListAdapterDataObject, newItem: TransactionListAdapterDataObject): Boolean {
            return (oldItem.transactionId == newItem.transactionId)
        }

        override fun areContentsTheSame(oldItem: TransactionListAdapterDataObject, newItem: TransactionListAdapterDataObject): Boolean {
            return ((oldItem.transactionAmount == newItem.transactionAmount) && (oldItem.transactionTime == newItem.transactionTime) &&
                    (oldItem.transactionType == newItem.transactionType) && (oldItem.transactionAccountId == newItem.transactionAccountId) &&
                    (oldItem.transactionBudgetId == newItem.transactionBudgetId) && (oldItem.transactionAccountTransferTo == newItem.transactionAccountTransferTo) &&
                    (oldItem.transactionAccountName == newItem.transactionAccountName) && (oldItem.transactionBudgetName == newItem.transactionBudgetName) &&
                    (oldItem.transactionAccountTransferToName == newItem.transactionAccountTransferToName))
        }

    }
}