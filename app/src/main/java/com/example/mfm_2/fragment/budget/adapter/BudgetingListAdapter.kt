package com.example.mfm_2.fragment.budget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Budget
import com.google.android.material.textfield.TextInputEditText

class BudgetingListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<BudgetingListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var budget = emptyList<Budget>()

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val budgetName: TextView = v.findViewById(R.id.textView_budget)
        val budgetAllocation: TextInputEditText = v.findViewById(R.id.textinput_budget_allocation)
        val budgetGoal: TextView = v.findViewById(R.id.textView_goal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_budgeting_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return budget.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = budget[position]
        holder.budgetName.text = current.budgetName
        holder.budgetGoal.text = current.budgetGoal.toString()
    }

    fun setData(budget: List<Budget>) {
        this.budget = budget
        notifyDataSetChanged()
    }

}