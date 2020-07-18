package com.example.mfm_2.fragment.budget

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.pojo.BudgetPieChartDataObject
import com.example.mfm_2.viewmodel.MFMViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class BudgetDetailActivity : AppCompatActivity(), OnChartValueSelectedListener {
    private lateinit var mfmViewModel: MFMViewModel
    private lateinit var chart: PieChart
    private lateinit var chipGroupG: ChipGroup
    private var budgetTypeFilterChip: MutableMap<Long, Boolean> = mutableMapOf(1L to true, 2L to true, 3L to true)
    private lateinit var budgetChartData: List<BudgetPieChartDataObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_detail)
        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)
        val chipGroup: ChipGroup = findViewById(R.id.chip_group1)
        chipGroupG = findViewById(R.id.chip_group1)
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
            updateChart()
        }

        setOnChipClickListener(findViewById(R.id.chip_monthly), 1L)
        setOnChipClickListener(findViewById(R.id.chip_yearly), 2L)
        setOnChipClickListener(findViewById(R.id.chip_goal), 3L)
    }

    private fun setOnChipClickListener(chip: Chip, budgetType: Long) {
        chip.setOnCheckedChangeListener { buttonView, isChecked ->
            budgetTypeFilterChip[budgetType] = isChecked
            updateChart()
        }
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
//        Log.i("haha", "Value: " + e!!.y.toString() + ", index: " + h!!.x.toString() + ", DataSet index: " + h.dataSetIndex)
        val data = budgetChartData.find {
            it.budgetName == chart.data.dataSet.getEntryForIndex(h!!.x.toInt()).label
        }
        val text1: MaterialTextView = findViewById(R.id.text1)
        val text2: MaterialTextView = findViewById(R.id.text2)
        val text3: MaterialTextView = findViewById(R.id.text3)

        data?.let {
            text1.text = it.budgetName
            text1.visibility = View.VISIBLE
            text2.text = "Total expense: RM " + it.budgetTransactionTotal
            text2.visibility = View.VISIBLE
            val budgetAvgEx = when (findViewById<Chip>(chipGroupG.checkedChipId).text?:1) {
                "This Month" -> {
                    0.0
                }
                "Last 3 Month" -> {
                    (it.budgetTransactionTotal / 3)
                }
                "This Year" -> {
                    (it.budgetTransactionTotal / 12)
                }
                else -> {
                    0.0
                }
            }
            if (budgetAvgEx > 0.0) {
                text3.text = "Monthly average expense: RM " + budgetAvgEx
                text3.visibility = View.VISIBLE
            } else {
                text3.visibility = View.INVISIBLE
            }
        }
    }

    private fun updateChart() {
        CoroutineScope(Dispatchers.IO).launch {
            val entries = ArrayList<PieEntry>()
            var budgetType: MutableList<Long> = mutableListOf()
            for (d in budgetTypeFilterChip) {
                if (d.value) {
                    budgetType.add(d.key)
                }
            }
            val query = when (chipGroupG.checkedChipId) {
                R.id.chip1 -> {
                    val timeFrom = Calendar.getInstance()
                    timeFrom.set(timeFrom.get(Calendar.YEAR), timeFrom.get(Calendar.MONTH), 1, 0, 0, 0)
                    val timeTo = Calendar.getInstance()
                    timeTo.timeInMillis = timeFrom.timeInMillis
                    timeTo.add(Calendar.MONTH, 1)
                    timeTo.add(Calendar.SECOND, -1)
                    mfmViewModel.getBudgetDetail(timeFrom, timeTo, budgetType)
                }
                R.id.chip2 -> {
                    val timeFrom = Calendar.getInstance()
                    timeFrom.set(timeFrom.get(Calendar.YEAR), timeFrom.get(Calendar.MONTH), 1, 0, 0, 0)
                    val timeTo = Calendar.getInstance()
                    timeTo.timeInMillis = timeFrom.timeInMillis
                    timeFrom.add(Calendar.MONTH, -2)
                    timeTo.add(Calendar.MONTH, 1)
                    timeTo.add(Calendar.SECOND, -1)
                    mfmViewModel.getBudgetDetail(timeFrom, timeTo, budgetType)
                }
                R.id.chip3 -> {
                    val timeFrom = Calendar.getInstance()
                    timeFrom.set(timeFrom.get(Calendar.YEAR), 0, 1, 0, 0, 0)
                    val timeTo = Calendar.getInstance()
                    timeTo.timeInMillis = timeFrom.timeInMillis
                    timeTo.add(Calendar.MONTH, 12)
                    timeTo.add(Calendar.SECOND, -1)
                    mfmViewModel.getBudgetDetail(timeFrom, timeTo, budgetType)
                }
                R.id.chip4 -> {
                    mfmViewModel.getBudgetDetail(budgetType)
                }
                else -> {
                    mfmViewModel.getBudgetDetail()
                }
            }
            budgetChartData = query
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
