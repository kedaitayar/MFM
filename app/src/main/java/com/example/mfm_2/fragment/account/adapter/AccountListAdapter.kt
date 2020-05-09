package com.example.mfm_2.fragment.account.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Account

class AccountListAdapter internal constructor(context: Context) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: OnItemClickListener? = null
    private var account = emptyList<Account>()

    interface OnItemClickListener {
        fun onPopupMenuButtonClick(account: Account)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
            popupMenuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onPopupMenuButtonClick(account[position])
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

    override fun getItemCount(): Int {
        return account.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = account[position]
        holder.accountName.text = current.accountName
        holder.accountBalance.text = current.accountBalance.toString()
        holder.accountAllocationBalance.text = "TODO"
    }

    fun setAccount(account: List<Account>) {
        this.account = account
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}