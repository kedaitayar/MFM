package com.example.mfm_2.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.pojo.TransactionWithAccountBudget
import java.util.*

class TransactionListAdapter internal constructor(context: Context) : RecyclerView.Adapter<TransactionListAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var transaction = emptyList<TransactionWithAccountBudget>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(v : View) : RecyclerView.ViewHolder(v) {
        init {
            v.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    listener?.onItemClick(transaction[position])
                }
            }
        }

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
        val current = transaction[position]
        holder.transactionAccount.text = current.accountName
        holder.transactionAmount.text = getCurrency() + current.transactionAmount.toString()
        holder.transactionBudget.text = current.budgetName
        holder.transactionDate.text = current.transactionTime.get(Calendar.DAY_OF_MONTH).toString() + "/" + current.transactionTime.get(Calendar.MONTH).toString() + "/" + current.transactionTime.get(Calendar.YEAR).toString()
    }

    fun setData(transaction: List<TransactionWithAccountBudget>){
        this.transaction = transaction
        notifyDataSetChanged()
    }

    fun getCurrency(): String {
        return "RM "
    }

    interface OnItemClickListener {
        fun onItemClick(transaction: TransactionWithAccountBudget)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}