package com.example.mfm_2.fragment.budget.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.BudgetTransaction
import com.example.mfm_2.model.Transaction

class BudgetListAdapter internal constructor(context: Context): RecyclerView.Adapter<BudgetListAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var budget = emptyList<Budget>()
    private var budgetTransaction = mutableMapOf<Long, BudgetTransaction>()
    private var transactionGrpByBudget = emptyList<Transaction>()
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onPopupMenuButtonClick(view: View, budget: Budget)
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        init {
            v.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener?.onItemClick(position)
                }
            }
            val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
            popupMenuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    listener?.onPopupMenuButtonClick(v, budget[position])
                }
            }
        }
        val budgetName: TextView = v.findViewById(R.id.textView10)
        val budgetAllocation: TextView = v.findViewById(R.id.textView12)
        val budgetGoal: TextView = v.findViewById(R.id.textView13)
        val budgetUsed: TextView = v.findViewById(R.id.textView11)
        val budgetType: TextView = v.findViewById(R.id.textView_type)
        val expandableView: ConstraintLayout = v.findViewById(R.id.constraint_layout_expandable_view)
        val expandableDetail: ConstraintLayout = v.findViewById(R.id.constraint_layout_expandable_detail)
        val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item_budget_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return budget.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = budget[position]
        holder.budgetName.text = current.budgetName
        holder.budgetAllocation.text = current.budgetAllocation.toString()
        holder.budgetGoal.text = current.budgetGoal.toString()
        holder.budgetUsed.text = (transactionGrpByBudget.find { it.transactionBudgetId == current.budgetId }?.transactionAmount?:0.0).toString()
        if (budgetTransaction[current.budgetId]?.budgetTransactionAmount == 0.0){
            holder.budgetAllocation.text = "0.0"
        } else {
            holder.budgetAllocation.text = (budgetTransaction[current.budgetId]?.budgetTransactionAmount?:0).toString()
        }
        holder.expandableDetail.visibility = if(current.isExpanded) View.VISIBLE else View.GONE
    }

    fun setData(budget: List<Budget>){
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

    fun setTransactionGrpByBudget(transaction: List<Transaction>){
        this.transactionGrpByBudget = transaction
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    fun flipIsExpanded(position: Int){
        budget[position].isExpanded = !budget[position].isExpanded
        notifyItemChanged(position)
    }

//    private class BudgetListDiffCallback : DiffUtil.ItemCallback<>
}