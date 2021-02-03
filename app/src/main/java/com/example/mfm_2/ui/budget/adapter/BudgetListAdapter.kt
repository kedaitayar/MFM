package com.example.mfm_2.ui.budget.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.`try`.Test02View
import com.example.mfm_2.`try`.Test03View
import com.example.mfm_2.entity.BudgetDeadline
import com.example.mfm_2.util.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.util.pojo.SelectedDate2
import kotlinx.android.synthetic.main.activity_test.view.*
import java.util.*

class BudgetListAdapter : RecyclerView.Adapter<BudgetListAdapter.ViewHolder>() {
    private val mDiffer = AsyncListDiffer<BudgetListAdapterDataObject>(this, BudgetListDiffCallback())
    private lateinit var budgetDeadline: List<BudgetDeadline>
    private lateinit var selectedDate: SelectedDate2
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
        val budgetAllocation: Test03View = v.findViewById(R.id.textView12)
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
        if (current.budgetTypeId == 3L) {
            holder.budgetUsed.text = (current.budgetGoal - current.budgetTotalPrevAllocation).toString()
            val cal = Calendar.getInstance()
            cal.set(selectedDate.year, selectedDate.month, 0,0,0)
            val currDeadline = budgetDeadline.find { it.budgetId == current.budgetId }
            val cal2 = currDeadline?.budgetDeadline?: Calendar.getInstance()
            val diffYear = cal2.get(Calendar.YEAR) - cal.get(Calendar.YEAR)
            val diffMonth = cal2.get(Calendar.MONTH) - cal.get(Calendar.MONTH)
            val budgetGoal = ((current.budgetGoal-current.budgetTotalPrevAllocation)/(diffMonth+(diffYear*12)))
            val goalPercentage = ((current.budgetAllocation / budgetGoal) * 100).toInt()
            holder.budgetAllocation.piePercent = goalPercentage
        } else {
            holder.budgetUsed.text = current.budgetUsed.toString()
            val goalPercentage = ((current.budgetAllocation / current.budgetGoal) * 100).toInt()
            holder.budgetAllocation.piePercent = goalPercentage
            if (goalPercentage >= 100) {
                holder.budgetAllocation.bgColor = ContextCompat.getColor(holder.budgetAllocation.context, R.color.gGreen)
            } else {
                holder.budgetAllocation.bgColor = ContextCompat.getColor(holder.budgetAllocation.context, R.color.gYellow)
            }
            if (current.budgetUsed <= current.budgetAllocation) {
                holder.budgetUsed.background.setTint(ContextCompat.getColor(holder.budgetUsed.context, R.color.gGreen))
            } else {
                holder.budgetUsed.background.setTint(ContextCompat.getColor(holder.budgetUsed.context, R.color.gRed))
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun submitDeadlineData(budgetDeadline: List<BudgetDeadline>) {
        this.budgetDeadline = budgetDeadline
    }

    fun submitSelectedDate(selectedDate: SelectedDate2) {
        this.selectedDate = selectedDate
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