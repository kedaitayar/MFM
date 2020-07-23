package com.example.mfm_2.ui.budget.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.mfm_2.model.BudgetType

class BudgetTypeDropdownAdapter(context: Context, private val layoutResource: Int, private val textViewResourceId: Int = 0, private val data: List<BudgetType>) : ArrayAdapter<BudgetType>(context, layoutResource, data) {

    override fun getItem(position: Int): BudgetType = data[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = createViewFromResource(convertView, parent, layoutResource)

        return bindData(getItem(position), view)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = createViewFromResource(convertView, parent, android.R.layout.simple_spinner_dropdown_item)

        return bindData(getItem(position), view)
    }

    private fun createViewFromResource(convertView: View?, parent: ViewGroup, layoutResource: Int): TextView {
        val context = parent.context
        val view = convertView ?: LayoutInflater.from(context).inflate(layoutResource, parent, false)
        return try {
            if (textViewResourceId == 0) view as TextView
            else {
                view.findViewById(textViewResourceId) ?:
                throw RuntimeException("Failed to find view with ID " +
                        "${context.resources.getResourceName(textViewResourceId)} in item layout")
            }
        } catch (ex: ClassCastException){
            Log.e("CustomArrayAdapter", "You must supply a resource ID for a TextView")
            throw IllegalStateException(
                "ArrayAdapter requires the resource ID to be a TextView", ex)
        }
    }

    private fun bindData(value: BudgetType, view: TextView): TextView {
        view.text = value.budgetTypeName
        return view
    }

    override fun getFilter(): Filter {
        return ArrayFilter()
    }

    private class ArrayFilter : Filter() {
        override fun performFiltering(prefix: CharSequence): FilterResults {
            return FilterResults()
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
        }

        override fun convertResultToString(resultValue: Any?): CharSequence {
//            return super.convertResultToString(resultValue)
            val result = resultValue as BudgetType
            return result.budgetTypeName
        }
    }

}