package com.example.mfm_2.fragment.home.adapter

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class TransactionListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TransactionListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var transaction = emptyList<TransactionWithAccountBudget>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            v.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(transaction[position])
                }
            }
        }

        val transactionAmount: TextView = v.findViewById(R.id.textView_transaction_amount)
        val transactionDate: TextView = v.findViewById(R.id.textView_date)
        val transactionBudget: TextView = v.findViewById(R.id.textView_budget)
        val transactionAccount: TextView = v.findViewById(R.id.textView_account)
    }

    interface OnItemClickListener {
        fun onItemClick(transaction: TransactionWithAccountBudget)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_transaction_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transaction.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = transaction[position]
        val resources: Resources = holder.itemView.context.resources
        val formatter = DecimalFormat(resources.getString(R.string.currency_format))
        val format = DecimalFormatSymbols(Locale.getDefault())
        format.groupingSeparator = ' '
        formatter.decimalFormatSymbols = format
        holder.transactionAccount.text = current.accountName
        holder.transactionAmount.text =
            resources.getString(R.string.currency, formatter.format(current.transactionAmount))
        holder.transactionBudget.text = current.budgetName
        holder.transactionDate.text = resources.getString(
            R.string.date,
            current.transactionTime.get(Calendar.DAY_OF_MONTH),
            current.transactionTime.get(Calendar.MONTH),
            current.transactionTime.get(Calendar.YEAR)
        )
    }

    fun setData(transaction: List<TransactionWithAccountBudget>) {
        this.transaction = transaction
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}