package com.example.mfm_2.fragment.budget

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.viewmodel.MFMViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class BudgetDetailActivity : AppCompatActivity(), OnChartValueSelectedListener {
    private lateinit var mfmViewModel: MFMViewModel
    private lateinit var chart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_detail)
        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)
        val chipGroup: ChipGroup = findViewById(R.id.chip_group1)

        chart = findViewById(R.id.chart)
        chart.description.isEnabled = false
        chart.setUsePercentValues(true)
        chart.isDrawHoleEnabled = false
        chart.animateY(1400, Easing.EaseInOutQuad)
        chart.setEntryLabelTextSize(24f)
        chart.setOnChartValueSelectedListener(this)
        chart.legend.isEnabled = false

//        val l = chart.legend
//        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        l.orientation = Legend.LegendOrientation.VERTICAL
//        l.setDrawInside(false)
//        l.xEntrySpace = 7f
//        l.yEntrySpace = 0f
//        l.yOffset = 0f
//        l.textSize = 18f

        updateChart()

        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.chip1 -> {
                    val timeFrom = Calendar.getInstance()
//                    timeFrom.set(year,month-1,1,0,0,0)
                    timeFrom.set(timeFrom.get(Calendar.YEAR), timeFrom.get(Calendar.MONTH), 1,0,0,0)
                    val timeTo = Calendar.getInstance()
                    timeTo.timeInMillis = timeFrom.timeInMillis
                    timeTo.add(Calendar.MONTH, 1)
                    timeTo.add(Calendar.SECOND, -1)
                    updateChart(timeFrom, timeTo)
                }
                R.id.chip2 -> {
                    val timeFrom = Calendar.getInstance()
                    timeFrom.set(timeFrom.get(Calendar.YEAR), timeFrom.get(Calendar.MONTH), 1,0,0,0)
                    val timeTo = Calendar.getInstance()
                    timeTo.timeInMillis = timeFrom.timeInMillis
                    timeFrom.add(Calendar.MONTH, -3)
                    timeTo.add(Calendar.MONTH, 1)
                    timeTo.add(Calendar.SECOND, -1)
                    updateChart(timeFrom, timeTo)
                }
                R.id.chip3 -> {
                    val timeFrom = Calendar.getInstance()
//                    timeFrom.set(timeFrom.get(Calendar.YEAR), 0, 1,0,0,0)
                    val timeTo = Calendar.getInstance()
//                    timeTo.timeInMillis = timeFrom.timeInMillis
                    timeFrom.add(Calendar.MONTH, -12)
                    updateChart(timeFrom, timeTo)
                }
                R.id.chip4 -> {
                    updateChart()
                }
            }
        }
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.i("haha", "Value: " + e!!.y.toString() + ", index: " + h!!.x.toString() + ", DataSet index: " + h.dataSetIndex)
        val test = chart.data.dataSet
        val test2 = test.getEntryForIndex(h.x.toInt())
        Log.i("haha", test2.label)
    }

    private fun updateChart() {
        CoroutineScope(Dispatchers.IO).launch {
            val entries = ArrayList<PieEntry>()
            val query = mfmViewModel.getBudgetDetail()
            for (q in query) {
                entries.add(PieEntry(q.budgetTransactionTotal.toFloat(), q.budgetName))
            }
            val pieDataSet = PieDataSet(entries, "Budget")
            for (c in ColorTemplate.MATERIAL_COLORS) {
                pieDataSet.colors.add(c)
            }
            val pieData = PieData(pieDataSet)
            pieData.setValueTextSize(20f)
            pieData.setValueFormatter(PercentFormatter(chart))
            chart.data = pieData
            chart.invalidate()
        }
    }

    private fun updateChart(timeFrom: Calendar, timeTo: Calendar) {
        Log.i("haha", "timeFrom:"+timeFrom.time.toString())
        Log.i("haha", "timeTo:"+timeTo.time.toString())
//        chart.clearValues()
        CoroutineScope(Dispatchers.IO).launch {
            val entries = ArrayList<PieEntry>()
            val query = mfmViewModel.getBudgetDetail(timeFrom, timeTo)
            Log.i("haha", query.toString())
            for (q in query) {
                entries.add(PieEntry(q.budgetTransactionTotal.toFloat(), q.budgetName))
            }
            val pieDataSet = PieDataSet(entries, "Budget")
            for (c in ColorTemplate.MATERIAL_COLORS) {
                pieDataSet.colors.add(c)
            }
            val pieData = PieData(pieDataSet)
            pieData.setValueTextSize(20f)
            pieData.setValueFormatter(PercentFormatter(chart))
            chart.data = pieData
            chart.invalidate()
        }
    }
}
