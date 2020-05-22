package com.example.mfm_2.fragment.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.pojo.AccountTransactionChartDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
//        val chart: BarChart = view.findViewById(R.id.chart)
        val chart: CombinedChart = view.findViewById(R.id.chart)
        val dataMap: MutableMap<Int, AccountTransactionChartDataObject> = mutableMapOf()

        val cal = Calendar.getInstance()
//        mfmViewModel.selectedDate.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
//            it?.let {
//                cal.set(it.year,it.month-1,1,0,0,0)
//            }
//        })

//        val data = BarData()
//        val data2 = LineData()
//        val combinedData = CombinedData()
//        combinedData.setData(BarData())
//        combinedData.setData(LineData())
//        chart.data = combinedData
        chart.data = CombinedData().apply {
            this.setData(BarData())
            this.setData(LineData())
        }

        mfmViewModel.accountTransactionChartData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                Log.i("haha", it.toString())
                val entries = mutableListOf<BarEntry>()
                val entries2 = mutableListOf<Entry>()
                for (data in it) {
                    dataMap[data.accountTransactionDate.substring(8).toInt()] = data
                }
                cal.set(mfmViewModel.selectedDate.value?.year ?: 0, mfmViewModel.selectedDate.value?.month ?: 0 - 1, 1, 0, 0, 0)
                Log.i("haha", cal.time.toString())
                var total: Double = 0.0
                for (i in 0 until cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    var moneyIn: Double = (dataMap[i]?.accountTransactionIncome ?: 0.0) + (dataMap[i]?.accountTransactionTransferIn ?: 0.0)
                    var moneyOut: Double = (dataMap[i]?.accountTransactionExpense ?: 0.0) + (dataMap[i]?.accountTransactionTransferOut ?: 0.0)
                    entries.add(BarEntry(i - 1f, floatArrayOf(moneyIn.toFloat(), -moneyOut.toFloat())))
                    total += moneyIn
                    total -= moneyOut
                    entries2.add(Entry(i - 1f, total.toFloat()))
                }
                val dataSet = BarDataSet(entries, "test")
                val barData = BarData(dataSet)
                val lineDataSet = LineDataSet(entries2, "test2")
                val lineData = LineData(lineDataSet)
//                val combinedData = CombinedData()
                val combinedData = chart.combinedData
                combinedData.setData(barData)
                combinedData.setData(lineData)
                combinedData.notifyDataChanged()
                chart.data = combinedData
                chart.notifyDataSetChanged()
                chart.postInvalidate()
            }
        })

//        CoroutineScope(Dispatchers.IO).launch {
//            val entries = mutableListOf<BarEntry>()
//            val entries2 = mutableListOf<Entry>()
//            val data = mfmViewModel.getAccountTransactionChartExpense()
//            val data2 = mfmViewModel.getAccountTransactionChartIncome()
//            for (d in data) {
//                dataMap[d.accountTransactionDate.substring(8).toInt()] = -d.accountTransactionAmount
//            }
//            for (d in data2) {
//                dataMap2[d.accountTransactionDate.substring(8).toInt()] = d.accountTransactionAmount
//            }
//            var total: Double = 0.0
//            for (i in 0 until maxDayOfMonth) {
//                entries.add(BarEntry(i-1f, floatArrayOf(dataMap[i]?.toFloat()?:0f, dataMap2[i]?.toFloat()?:0f)))
//                total += (dataMap2[i] ?: 0.0)
//                total += (dataMap[i] ?: 0.0)
//                entries2.add(Entry(i-1f, total.toFloat()))
////                if (dataMap[i] == null){
////                    entries.add(BarEntry(i-1f, 0f))
////                } else {
////                    entries.add(BarEntry(i-1f, dataMap[i]!!.toFloat()))
////                }
//            }
//            val dataSet = BarDataSet(entries, "test")
//            val barData = BarData(dataSet)
//            val lineDataSet = LineDataSet(entries2, "test2")
//            val lineData = LineData(lineDataSet)
//            val combinedData = CombinedData()
//            combinedData.setData(barData)
//            combinedData.setData(lineData)
////            chart.data = barData
//            chart.data = combinedData
//            chart.postInvalidate()
//        }
        return view
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
