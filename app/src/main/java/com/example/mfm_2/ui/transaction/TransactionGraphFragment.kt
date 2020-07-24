package com.example.mfm_2.ui.transaction

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.pojo.TransactionGraphDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.IsoFields
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransactionGraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransactionGraphFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction_graph, container, false)
        val chart: BarChart = view.findViewById(R.id.chart)

        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.legend.isEnabled = false
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTH_SIDED

        mfmViewModel.transactionGraphData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                updateChart(chart, it)
            }
        })

        return view
    }

    private fun updateChart(chart: BarChart, transactionGraphData: List<TransactionGraphDataObject>) {
        var entries: MutableList<BarEntry> = mutableListOf()
        val dataMapIncome: MutableMap<Long, TransactionGraphDataObject> = mutableMapOf()
        val dataMapExpense: MutableMap<Long, TransactionGraphDataObject> = mutableMapOf()
        for (d in transactionGraphData) {
            if (d.transactionType == "INCOME") {
                dataMapIncome[d.transactionWeek] = d
            } else if (d.transactionType == "EXPENSE") {
                dataMapExpense[d.transactionWeek] = d
            }
        }
        for (d in 1..53) {
            entries.add(
                BarEntry(
                    d.toFloat(),
                    floatArrayOf(
                        (dataMapIncome[d.toLong()]?.transactionAmount?.toFloat() ?: 0f),
                        -(dataMapExpense[d.toLong()]?.transactionAmount?.toFloat() ?: 0f)
                    )
                )
            )
        }
        val barDataSet = BarDataSet(entries, "test")
        barDataSet.colors = arrayListOf(Color.GREEN, Color.RED)
        barDataSet.setDrawValues(false)
        val barData = BarData(barDataSet)
        chart.data = barData
        chart.invalidate()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TransactionGraphFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionGraphFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}