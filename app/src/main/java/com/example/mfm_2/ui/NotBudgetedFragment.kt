package com.example.mfm_2.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.mfm_2.R
import com.example.mfm_2.viewmodel.MFMViewModel

/**
 * A simple [Fragment] subclass.
 */
class NotBudgetedFragment : Fragment() {
    private lateinit var mfmViewModel: MFMViewModel
    private var totalIncome: Double = 0.0
    private var totalBudgeted: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")
        val view = inflater.inflate(R.layout.fragment_not_budgeted, container, false)
        val toBeBudgeted: TextView = view.findViewById(R.id.textView_to_be_budgeted)
        mfmViewModel.totalIncome.observe(viewLifecycleOwner, Observer {
            it?.let {
                setAccountIncome(toBeBudgeted, it)
            }
        })
        mfmViewModel.totalBudgetedAmount.observe(viewLifecycleOwner, Observer {
            it?.let {
                setTotalBudgeted(toBeBudgeted, it)
            }
        })

        val info: Button = view.findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Not Budgeted")
                .setMessage("This toolbar display the total amount of fund that is still not budgeted. It is recommended that this amount is keep to the minimum.\n\nTo budget some fund, go to the budget page by swiping right to the budget page or tapping the budget page tab and then tap the budgeting button.")
                .setPositiveButton("OK") { dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }

        return view
    }

    private fun setAccountIncome(toBeBudgeted: TextView, totalIncome: Double) {
        this.totalIncome = totalIncome
        toBeBudgeted.text = (this.totalIncome - this.totalBudgeted).toString()
    }

    private fun setTotalBudgeted(toBeBudgeted: TextView, totalBudgeted: Double) {
        this.totalBudgeted = totalBudgeted
        toBeBudgeted.text = (this.totalIncome - this.totalBudgeted).toString()
    }
}
