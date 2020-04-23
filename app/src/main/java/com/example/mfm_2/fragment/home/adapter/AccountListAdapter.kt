package com.example.mfm_2.fragment.home.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Account
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

class AccountListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var account = emptyList<Account>()
    private var listener: OnItemClickListener? = null

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            v.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener?.onItemClick(account[position])
                }
            }
        }
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
        val resources: Resources = holder.itemView.context.resources
        val formatter = DecimalFormat(resources.getString(R.string.currency_format))
        val format = DecimalFormatSymbols(Locale.getDefault())
        format.groupingSeparator = ' '
        formatter.decimalFormatSymbols = format
        holder.textView1.text = current.accountName + " " + current.accountId
        holder.textView2.text = resources.getString(R.string.currency, formatter.format(current.accountBalance))
        holder.textView3.text = resources.getString(R.string.currency, formatter.format(current.accountAllocationBalance))
    }

    fun setData(account: List<Account>) {
        this.account = account
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}