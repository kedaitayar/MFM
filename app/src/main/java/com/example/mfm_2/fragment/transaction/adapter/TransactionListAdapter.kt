package com.example.mfm_2.fragment.transaction.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Account
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.Transaction
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class TransactionListAdapter internal constructor(context: Context) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: OnItemClickListener? = null
    private var transaction = emptyList<Transaction>()
    private var budget = emptyList<Budget>()
    private var account = emptyList<Account>()

    interface OnItemClickListener {
        fun onPopupMenuButtonClick(transaction: Transaction, popupMenuButton: Button)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
            popupMenuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onPopupMenuButtonClick(transaction[position], popupMenuButton)
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

    override fun getItemCount(): Int {
        return transaction.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTransaction = transaction[position]
        val currentAccount = account.find { it.accountId == currentTransaction.transactionAccountId }
        val currentBudget = budget.find { it.budgetId == currentTransaction.transactionBudgetId }
        val resources: Resources = holder.itemView.context.resources
        val formatter = DecimalFormat(resources.getString(R.string.currency_format))
        val format = DecimalFormatSymbols(Locale.getDefault())
        format.groupingSeparator = ' '
        formatter.decimalFormatSymbols = format
        when (currentTransaction.transactionType) {
            "EXPENSE" -> {
                if (currentAccount != null) {
                    holder.transactionAccount.text = currentAccount.accountName
                }
                if (currentBudget != null) {
                    holder.transactionBudget.text = currentBudget.budgetName
                }
                holder.transactionAccountTo.visibility = View.GONE
                holder.transactionAmount.setTextColor(Color.parseColor("#d50000"))
            }
            "INCOME" -> {
                if (currentAccount != null) {
                    holder.transactionAccount.text = currentAccount.accountName
                }
                holder.transactionAccountTo.visibility = View.GONE
                holder.transactionBudget.text = "Income"
                holder.transactionAmount.setTextColor(Color.parseColor("#00c853"))
            }
            "TRANSFER" -> {
                val currentAccountFrom = account.find { it.accountId == currentTransaction.transactionAccountId }
                val currentAccountTo = account.find { it.accountId == currentTransaction.transactionAccountTransferTo }
                if (currentAccountTo != null) {
                    holder.transactionAccountTo.text = currentAccountTo.accountName
                    holder.transactionAccountTo.visibility = View.VISIBLE
                }
                if (currentAccountFrom != null) {
                    holder.transactionAccount.text = currentAccountFrom.accountName
                }
                holder.transactionBudget.text = "Transfer"
                holder.transactionAmount.setTextColor(Color.parseColor("#000000"))
            }
        }
        holder.transactionAmount.text = resources.getString(R.string.currency, formatter.format(currentTransaction.transactionAmount))
        holder.transactionDate.text = resources.getString(
            R.string.date,
            currentTransaction.transactionTime.get(Calendar.DAY_OF_MONTH),
            currentTransaction.transactionTime.get(Calendar.MONTH),
            currentTransaction.transactionTime.get(Calendar.YEAR)
        )
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setTransaction(transaction: List<Transaction>) {
        this.transaction = transaction
        notifyDataSetChanged()
    }

    fun setAccount(account: List<Account>) {
        this.account = account
        notifyDataSetChanged()
    }

    fun setBudget(budget: List<Budget>) {
        this.budget = budget
        notifyDataSetChanged()
    }
}