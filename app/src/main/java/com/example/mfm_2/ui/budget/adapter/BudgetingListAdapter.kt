package com.example.mfm_2.ui.budget.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mfm_2.R
import com.example.mfm_2.entity.BudgetDeadline
import com.example.mfm_2.util.pojo.BudgetListAdapterDataObject
import com.example.mfm_2.util.pojo.SelectedDate2
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class BudgetingListAdapter internal constructor(context: Context) :
    ListAdapter<BudgetListAdapterDataObject, BudgetingListAdapter.ViewHolder>(BudgetListDiffCallback()) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    var budgetAlloc = mutableMapOf<Int, String>()
    private lateinit var budgetDeadline: List<BudgetDeadline>
    private lateinit var selectedDate: SelectedDate2

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
                    if (s.isNullOrBlank()) {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.budgetName.text = current.budgetName
        holder.budgetAllocation.setText(current.budgetAllocation.toString())
        if (current.budgetTypeId != 3L) {
            holder.budgetGoal.text = current.budgetGoal.toString()
        } else {
            val cal = Calendar.getInstance()
            cal.set(selectedDate.year, selectedDate.month, 0,0,0)
            val currDeadline = budgetDeadline.find { it.budgetId == current.budgetId }
            val cal2 = currDeadline?.budgetDeadline?:Calendar.getInstance()
            val diffYear = cal2.get(Calendar.YEAR) - cal.get(Calendar.YEAR)
            val diffMonth = cal2.get(Calendar.MONTH) - cal.get(Calendar.MONTH)
            holder.budgetGoal.text = ((current.budgetGoal-current.budgetTotalPrevAllocation)/(diffMonth+(diffYear*12))).toString()
        }
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
}