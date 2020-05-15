package com.example.mfm_2.fragment.budget.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.BudgetTransaction
import com.google.android.material.textfield.TextInputEditText

class BudgetingListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<BudgetingListAdapter.ViewHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var budget = emptyList<Budget>()
    private var budgetTransaction = mutableMapOf<Long, BudgetTransaction>()
    var budgetAlloc = mutableMapOf<Int, String>()


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            val budgetAllocation: TextInputEditText = v.findViewById(R.id.textinput_budget_allocation)
            budgetAllocation.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    budgetAlloc[adapterPosition] = s.toString()
                     if (s.isNullOrBlank()){
                         budgetAlloc[adapterPosition] = "0"
                     } else {
                         budgetAlloc[adapterPosition] = s.toString()
                     }
                }

            })
        }

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
        if (budgetTransaction[current.budgetId]?.budgetTransactionAmount == 0.0){
            holder.budgetAllocation.setText("")
        } else {
            holder.budgetAllocation.setText((budgetTransaction[current.budgetId]?.budgetTransactionAmount?:0).toString())
        }
    }

    fun setBudget(budget: List<Budget>) {
        this.budget = budget
        notifyDataSetChanged()
    }

    fun setBudgetTransaction(budgetTransaction: List<BudgetTransaction>) {
        this.budgetTransaction.clear()
        for (budgetTransaction in budgetTransaction){
            this.budgetTransaction[budgetTransaction.budgetTransactionBudgetId] = budgetTransaction
        }
        notifyDataSetChanged()
    }

    fun getData(): List<Budget> {
        return budget
    }
}