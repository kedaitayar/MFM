package com.example.mfm_2.ui.account

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.util.pojo.AccountTransactionChartDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountGraphFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountGraphFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private lateinit var mfmViewModel: MFMViewModel
//    private var accountListPrevMonth: List<AccountListAdapterDataObject> = emptyList()
//    private var accountTransactionChartDataObject: List<AccountTransactionChartDataObject> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = activity?.run { ViewModelProvider(this).get(MFMViewModel::class.java) } ?: throw Exception("Invalid Activity")
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account_graph, container, false)
        val chart: CombinedChart = view.findViewById(R.id.chart)
        chart.description.isEnabled = false
        chart.setDrawGridBackground(false)
        chart.drawOrder = arrayOf(DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER)
        chart.data = CombinedData().apply {
            this.setData(BarData())
            this.setData(LineData())
        }
        chart.legend.isEnabled = false
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTH_SIDED

        mfmViewModel.accountTransactionChartData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
//                accountTransactionChartDataObject = it
                updateGraph(chart, it)
            }
        })

        val info: Button = view.findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Account Monthly Trend")
                .setMessage("This is graph show the income and expense of selected account in the selected date.\nTo select account, click the account box in the Account card box.\nTo select the date, click the date dropdown located at the toolbar.")
                .setPositiveButton("OK") { dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }

        return view
    }

    private fun updateGraph(chart: CombinedChart, accountTransactionChartDataObject: List<AccountTransactionChartDataObject>) {
        val dataMap: MutableMap<Int, AccountTransactionChartDataObject> = mutableMapOf()
        val cal = Calendar.getInstance()
        val entries = mutableListOf<BarEntry>()
        val entries2 = mutableListOf<Entry>()
        dataMap.clear()
        for (data in accountTransactionChartDataObject) {
            dataMap[data.accountTransactionDate.substring(8).toInt()] = data
        }
        cal.set(mfmViewModel.selectedDate.value?.year ?: 0, mfmViewModel.selectedDate.value?.month ?: 0 - 1, 1, 0, 0, 0)
        val balancePrevMonth = (accountTransactionChartDataObject.firstOrNull()?.accountTransactionIncomePrevMonth ?: 0.0) - (accountTransactionChartDataObject.firstOrNull()?.accountTransactionExpensePrevMonth ?: 0.0) - (accountTransactionChartDataObject.firstOrNull()?.accountTransactionTransferOutPrevMonth ?: 0.0) + (accountTransactionChartDataObject.firstOrNull()?.accountTransactionTransferInPrevMonth ?: 0.0)
        var total: Double = balancePrevMonth
        for (i in 1 until cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            var moneyIn: Double = (dataMap[i]?.accountTransactionIncome ?: 0.0) + (dataMap[i]?.accountTransactionTransferIn ?: 0.0)
            var moneyOut: Double = (dataMap[i]?.accountTransactionExpense ?: 0.0) + (dataMap[i]?.accountTransactionTransferOut ?: 0.0)
            entries.add(BarEntry(i - 1f, floatArrayOf(moneyIn.toFloat(), -moneyOut.toFloat())))
            total += moneyIn
            total -= moneyOut

            entries2.add(Entry(i - 1f, total.toFloat()))
        }
        val barDataSet = BarDataSet(entries, "test")
        barDataSet.colors = arrayListOf(Color.GREEN, Color.RED)
        barDataSet.setDrawValues(false)
        val barData = BarData(barDataSet)
        val lineDataSet = LineDataSet(entries2, "test2")
        lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        lineDataSet.lineWidth = 1.5f
        lineDataSet.color = Color.BLACK
        lineDataSet.setCircleColor(Color.BLACK)
        lineDataSet.setDrawValues(false)
        val lineData = LineData(lineDataSet)
        val combinedData = CombinedData()
        combinedData.setData(barData)
        combinedData.setData(lineData)
        combinedData.notifyDataChanged()
        chart.data = combinedData
        chart.notifyDataSetChanged()
        chart.invalidate()
        chart.animateX(500)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountGraphFragment.
         */
        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            AccountGraphFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
    }
}
