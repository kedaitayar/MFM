package com.example.mfm_2.fragment.budget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.fragment.home.adapter.TransactionListAdapter
import com.example.mfm_2.model.Budget

class BudgetListAdapter internal constructor(context: Context): RecyclerView.Adapter<BudgetListAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var budget = emptyList<Budget>()
    private var listener: TransactionListAdapter.OnItemClickListener? = null
    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_budget_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}