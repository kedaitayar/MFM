package com.example.mfm_2.fragment.transaction.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Account
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class TransactionListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var transaction = emptyList<Transaction>()
    private var budget = emptyList<Budget>()
    private var account = emptyList<Account>()
//    private var listener: OnItemClickListener? = null

//    interface OnItemClickListener {
//        fun onItemClick(transaction: TransactionWithAccountBudget)
//    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
//            init {
//                v.setOnClickListener {
//                    val position = adapterPosition
//                    if (position != RecyclerView.NO_POSITION) {
//                        listener?.onItemClick(transaction[position])
//                    }
//                }
//            }

        val transactionAmount: TextView = v.findViewById(R.id.textView_transaction_amount)
        val transactionDate: TextView = v.findViewById(R.id.textView_date)
        val transactionBudget: TextView = v.findViewById(R.id.textView_budget)
        val transactionAccount: TextView = v.findViewById(R.id.textView_account)
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
        val currentAccount = account.find { it.accountId == currentTransaction.transactionAccountId }!!
        val currentBudget = budget.find { it.budgetId == currentTransaction.transactionBudgetId }!!
        val resources: Resources = holder.itemView.context.resources
        val formatter = DecimalFormat(resources.getString(R.string.currency_format))
        val format = DecimalFormatSymbols(Locale.getDefault())
        format.groupingSeparator = ' '
        formatter.decimalFormatSymbols = format
        holder.transactionAccount.text = currentAccount.accountName
        holder.transactionAmount.text =
            resources.getString(R.string.currency, formatter.format(currentTransaction.transactionAmount))
        holder.transactionBudget.text = currentBudget.budgetName
        holder.transactionDate.text = resources.getString(
            R.string.date,
            currentTransaction.transactionTime.get(Calendar.DAY_OF_MONTH),
            currentTransaction.transactionTime.get(Calendar.MONTH),
            currentTransaction.transactionTime.get(Calendar.YEAR)
        )
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

//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        this.listener = listener
//    }
}