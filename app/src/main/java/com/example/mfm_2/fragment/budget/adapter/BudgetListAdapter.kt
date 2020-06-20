package com.example.mfm_2.fragment.budget.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.pojo.BudgetListAdapterDataObject

class BudgetListAdapter : RecyclerView.Adapter<BudgetListAdapter.ViewHolder>() {
    private val mDiffer = AsyncListDiffer<BudgetListAdapterDataObject>(this, BudgetListDiffCallback())
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onPopupMenuButtonClick(view: View, budgetListAdapterDataObject: BudgetListAdapterDataObject)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
            popupMenuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onPopupMenuButtonClick(v, mDiffer.currentList[position])
                }
            }
        }

        val budgetName: TextView = v.findViewById(R.id.textView10)
        val budgetAllocation: TextView = v.findViewById(R.id.textView12)
        val budgetUsed: TextView = v.findViewById(R.id.textView11)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_budget_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = mDiffer.currentList[position]
        holder.budgetName.text = current.budgetName
        holder.budgetAllocation.text = current.budgetAllocation.toString()
        holder.budgetUsed.text = current.budgetUsed.toString()
        holder.budgetAllocation.text = current.budgetAllocation.toString()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    private class BudgetListDiffCallback : DiffUtil.ItemCallback<BudgetListAdapterDataObject>() {
        override fun areItemsTheSame(oldItem: BudgetListAdapterDataObject, newItem: BudgetListAdapterDataObject): Boolean {
//            return (oldItem.budgetId == newItem.budgetId)
            return oldItem.budgetId == newItem.budgetId
        }

        override fun areContentsTheSame(oldItem: BudgetListAdapterDataObject, newItem: BudgetListAdapterDataObject): Boolean {
//            return ((oldItem.budgetName == newItem.budgetName) && (oldItem.budgetAllocation == newItem.budgetAllocation) && (oldItem.budgetGoal == newItem.budgetGoal) && (oldItem.budgetTypeId == newItem.budgetTypeId) && (oldItem.budgetTypeName == newItem.budgetTypeName) && (oldItem.isExpanded == newItem.isExpanded))
            return oldItem == newItem
        }

    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    fun submitData(budgetList: List<BudgetListAdapterDataObject>) {
        mDiffer.submitList(budgetList)
    }
}