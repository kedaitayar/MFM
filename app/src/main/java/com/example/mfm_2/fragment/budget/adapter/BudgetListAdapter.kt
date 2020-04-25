package com.example.mfm_2.fragment.budget.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.fragment.home.adapter.TransactionListAdapter
import com.example.mfm_2.model.Budget

class BudgetListAdapter internal constructor(context: Context): RecyclerView.Adapter<BudgetListAdapter.ViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var budget = emptyList<Budget>()
    private var listener: OnItemClickListener? = null

    interface OnItemClickListener{
        fun onItemClick(budget: Budget, view: View)
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
//        init {
//            v.setOnClickListener {
//                val position = adapterPosition
//                if (position != RecyclerView.NO_POSITION){
//                    listener?.onItemClick(budget[position], v)
//                }
//            }
//        }
        val budgetName: TextView = v.findViewById(R.id.textView10)
        val budgetAllocation: TextView = v.findViewById(R.id.textView12)
        val budgetGoal: TextView = v.findViewById(R.id.textView16)
        val expandableView: ConstraintLayout = v.findViewById(R.id.constraint_layout_expandable_view)
        val expandableDetail: ConstraintLayout = v.findViewById(R.id.constraint_layout_expandable_detail)
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


        holder.expandableDetail.visibility = if(current.isExpanded) View.VISIBLE else View.GONE
        holder.expandableView.setOnClickListener(View.OnClickListener {
            budget[position].isExpanded = !current.isExpanded
            notifyItemChanged(position)
        })
    }

    fun setData(budget: List<Budget>){
        this.budget = budget
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}