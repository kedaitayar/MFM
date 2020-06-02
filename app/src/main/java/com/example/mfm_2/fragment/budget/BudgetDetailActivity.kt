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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class BudgetDetailActivity : AppCompatActivity(), OnChartValueSelectedListener {
    private lateinit var mfmViewModel: MFMViewModel
    private val chart: PieChart = findViewById(R.id.chart)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_detail)
        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)

        val filter1: AutoCompleteTextView = findViewById(R.id.dropdown_filter1)
        val filter1Option: MutableList<String> = mutableListOf("This Month", "All Time")
        val filter1Adapter = ArrayAdapter<String>(this, R.layout.dropdown_menu_popup_item, filter1Option)
        filter1.setAdapter(filter1Adapter)
        filter1.setText(filter1.adapter.getItem(0).toString(), false)

        filter1.setOnItemClickListener { parent, view, position, id ->
            if (position == 0) {

            } else if (position == 1) {

            }
        }

        chart.description.isEnabled = false
        chart.setUsePercentValues(true)
        chart.isDrawHoleEnabled = false
        chart.animateY(1400, Easing.EaseInOutQuad)
        chart.setEntryLabelTextSize(24f)
        chart.setOnChartValueSelectedListener(this)

//        val l = chart.legend
//        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//        l.orientation = Legend.LegendOrientation.VERTICAL
//        l.setDrawInside(false)
//        l.xEntrySpace = 7f
//        l.yEntrySpace = 0f
//        l.yOffset = 0f
//        l.textSize = 18f

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

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Log.i("haha", "Value: " + e!!.y.toString() + ", index: " + h!!.x.toString() + ", DataSet index: " + h.dataSetIndex)
        val test = chart.data.dataSet
        val test2 = test.getEntryForIndex(h.x.toInt())
        Log.i("haha", test2.label)
    }

    private fun updateChart(position: Int) {
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
}
