package com.example.mfm_2.fragment.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Account

class AccountListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var account = emptyList<Account>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val textView1: TextView = v.findViewById(R.id.textView4)
        val textView2: TextView = v.findViewById(R.id.textView5)
        val textView3: TextView = v.findViewById(R.id.textView7)
    }

    interface OnItemClickListener {
        fun onItemClick(account: Account)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AccountListAdapter.ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_account_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return account.size
    }

    override fun onBindViewHolder(holder: AccountListAdapter.ViewHolder, position: Int) {
        val current = account[position]
        holder.textView1.text = current.accountName + " " + current.accountId
        holder.textView2.text = getCurrency() + current.accountBalance.toString()
        holder.textView3.text = getCurrency() + current.accountAllocationBalance.toString()
    }

    fun getCurrency(): String {
        return "RM "
    }

    fun setData(account: List<Account>) {
        this.account = account
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}