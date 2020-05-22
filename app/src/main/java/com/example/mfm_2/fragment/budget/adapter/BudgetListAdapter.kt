package com.example.mfm_2.fragment.budget.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.model.Budget
import com.example.mfm_2.model.BudgetTransaction
import com.example.mfm_2.model.Transaction
import com.example.mfm_2.pojo.BudgetListAdapterDataObject

//class BudgetListAdapter internal constructor(context: Context) : ListAdapter<BudgetListAdapterDataObject, BudgetListAdapter.ViewHolder>(BudgetListDiffCallback()) {
class BudgetListAdapter internal constructor(context: Context) : RecyclerView.Adapter<BudgetListAdapter.ViewHolder>() {
    private val mDiffer = AsyncListDiffer<BudgetListAdapterDataObject>(this, BudgetListDiffCallback())
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onPopupMenuButtonClick(view: View, budgetListAdapterDataObject: BudgetListAdapterDataObject)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        init {
            v.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
//                    listener?.onItemClick(position)
                    expandedOrCollapse(position)
                }
            }
            val popupMenuButton: Button = v.findViewById(R.id.button_popup_menu)
            popupMenuButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
//                    listener?.onPopupMenuButtonClick(v, budgetList[position])
                    listener?.onPopupMenuButtonClick(v, mDiffer.currentList[position])
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        Log.i("haha", position.toString())
        val current = mDiffer.currentList[position]
        holder.expandableDetail.visibility = if (current.isExpanded) View.VISIBLE else View.GONE
        holder.budgetName.text = current.budgetName
        holder.budgetAllocation.text = current.budgetAllocation.toString()
        holder.budgetGoal.text = current.budgetGoal.toString()
        holder.budgetUsed.text = current.budgetUsed.toString()
        holder.budgetAllocation.text = current.budgetAllocation.toString()
        holder.budgetType.text = current.budgetTypeName
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun expandedOrCollapse(position: Int) {
        val budgetList = mDiffer.currentList.toMutableList()
        budgetList[position] = budgetList[position].copy(isExpanded = !budgetList[position].isExpanded)
        mDiffer.submitList(budgetList)
    }

    private class BudgetListDiffCallback : DiffUtil.ItemCallback<BudgetListAdapterDataObject>() {
        override fun areItemsTheSame(oldItem: BudgetListAdapterDataObject, newItem: BudgetListAdapterDataObject): Boolean {
//            return (oldItem.budgetId == newItem.budgetId)
            return oldItem.budgetId == newItem.budgetId
        }

        override fun areContentsTheSame(oldItem: BudgetListAdapterDataObject, newItem: BudgetListAdapterDataObject): Boolean {
            return ((oldItem.budgetName == newItem.budgetName) && (oldItem.budgetAllocation == newItem.budgetAllocation) && (oldItem.budgetGoal == newItem.budgetGoal) && (oldItem.budgetTypeId == newItem.budgetTypeId) && (oldItem.budgetTypeName == newItem.budgetTypeName) && (oldItem.isExpanded == newItem.isExpanded))
//            return oldItem == newItem
        }

    }

    override fun getItemCount(): Int {
//        return budgetList.size
        return mDiffer.currentList.size
    }

    fun submitData(budgetList: List<BudgetListAdapterDataObject>) {
//        this.budgetList = budgetList
        mDiffer.submitList(budgetList)
    }
}