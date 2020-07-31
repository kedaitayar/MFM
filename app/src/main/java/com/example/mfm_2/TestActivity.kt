package com.example.mfm_2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.example.mfm_2.singleton.SelectedDateSingleton
import com.example.mfm_2.ui.NotBudgetedFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.Chart.PAINT_GRID_BACKGROUND
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import java.util.*

/**
 * The purpose of this activity is for testing component or code before implementing it to the app
 */

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val chart: LineChart = findViewById(R.id.chart1)
        val chart2: BarChart = findViewById(R.id.chart2)
//        Log.i("test", "hoho")
        var entries: MutableList<Entry> = mutableListOf()
        var entries2: MutableList<BarEntry> = mutableListOf()
        var data = listOf<Data>(Data(1f, 1f), Data(2f, 2f), Data(3f, 3f), Data(4f, 2f), Data(5f, 1f))
        var data2 = arrayOf<Array<Int>>(arrayOf(1, 2, 3, 4, 5), arrayOf(1, 2, 3, 4, 5))
        var data3 = arrayOf(
            1,
            9,
            3,
            9,
            10,
            4,
            3,
            5,
            9,
            6,
            1,
            6,
            7,
            9,
            2,
            10,
            5,
            2,
            5,
            5,
            2,
            2,
            5,
            8,
            5,
            1,
            7,
            2,
            10,
            4,
            1,
            9,
            3,
            3,
            10,
            5,
            9,
            8,
            8,
            3,
            4,
            9,
            7,
            3,
            10,
            9,
            5,
            7,
            9,
            4,
            6,
            10,
            9,
            6,
            5,
            4,
            8,
            3,
            4,
            3,
            6,
            2,
            7,
            4,
            7,
            1,
            7,
            10,
            8,
            2,
            10,
            4,
            6,
            7,
            8,
            9,
            1,
            2,
            1,
            7,
            1,
            6,
            10,
            1,
            1,
            9,
            8,
            3,
            6,
            3,
            10,
            9,
            5,
            4,
            10,
            9,
            10,
            9,
            5,
            8
        )
        var data4 = arrayOf(
            2,
            1,
            2,
            2,
            1,
            1,
            2,
            1,
            2,
            1,
            1,
            1,
            2,
            2,
            2,
            2,
            2,
            1,
            2,
            1,
            2,
            1,
            2,
            2,
            2,
            1,
            2,
            1,
            1,
            1,
            2,
            1,
            1,
            2,
            1,
            2,
            1,
            2,
            2,
            1,
            1,
            2,
            2,
            1,
            2,
            2,
            2,
            1,
            1,
            1,
            2,
            2,
            1,
            1,
            2,
            1,
            1,
            2,
            1,
            1,
            2,
            2,
            1,
            2,
            1,
            1,
            1,
            2,
            2,
            2,
            2,
            2,
            1,
            1,
            1,
            2,
            1,
            1,
            2,
            2,
            1,
            1,
            1,
            2,
            1,
            2,
            2,
            2,
            2,
            2,
            2,
            2,
            2,
            1,
            2,
            1,
            1,
            2,
            2,
            2
        )
        var data5 = mutableListOf<Int>()
        var data6 = mutableListOf<Int>()
        var total = 50
        var i = 0
        while (i < 30) {
            if (data4[i] == 2) {
                total -= data3[i]
            } else {
                total += data3[i]
            }
            data5.add(i)
            data6.add(total)
            entries.add(Entry(i.toFloat(), total.toFloat()))
            entries2.add(BarEntry(i.toFloat(), total.toFloat()))
            i++
        }

//        for (data in data){
//            entries.add(Entry(data.x,data.y))
//        }
//        Log.i("test", entries.toString())
        var dataSet: LineDataSet = LineDataSet(entries, "test")
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.lineWidth = 0f
        dataSet.fillAlpha = 255
        dataSet.setDrawCircles(false)
        dataSet.setDrawFilled(true)
        dataSet.setDrawHighlightIndicators(true)
        dataSet.setDrawHighlightIndicators(false)
        dataSet.color = Color.GREEN
        dataSet.fillColor = dataSet.color

        var dataSet2 = BarDataSet(entries2, "test")


        var lineData: LineData = LineData(dataSet)
        lineData.setDrawValues(false)

        var barData = BarData(dataSet2)

        var xAxis = chart.xAxis
        var leftAxis = chart.axisLeft
        var rightAxis = chart.axisRight

        leftAxis.setDrawGridLines(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawGridLinesBehindData(false)
        rightAxis.gridLineWidth = 1f

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 14f

        chart2.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart2.xAxis.setDrawGridLines(false)
        chart2.axisLeft.setDrawGridLines(false)
        chart2.axisRight.setDrawGridLines(false)


        chart.data = lineData
        chart.description.isEnabled = false
        chart.extraBottomOffset = 2f
        chart.legend.isEnabled = false
//        chart.setBackgroundColor(ColorTemplate.MATERIAL_COLORS[Random.nextInt(ColorTemplate.MATERIAL_COLORS.size)])
        chart.setDrawGridBackground(true)
        var paint = chart.getPaint(PAINT_GRID_BACKGROUND)
        paint.color = Color.RED
        chart.setPaint(paint, PAINT_GRID_BACKGROUND)

        chart.invalidate()

        chart2.data = barData
        chart2.description.isEnabled = false
        chart2.legend.isEnabled = false

        //progress bar//
        val progressbar: ProgressBar = findViewById(R.id.progressBar)
        progressbar.progress = 75

        val testText: TextView = findViewById(R.id.textView_test)

//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//        val button: Button = findViewById(R.id.buttontest)
//        button.setOnClickListener {
////            val pickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
////            }, year, month, day)
////            pickerDialog.show()
//
//            val pickerDialog = MonthYearPickerDialog()
//            pickerDialog.setListener(DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->  })
//            pickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
//        }

        val date = SelectedDateSingleton.singletonSelectedDate
        val c2 = Calendar.getInstance()
        c2.set(date.year, date.month - 1, 1, 0, 0, 0)
        Log.i("haha", c2.timeInMillis.toString())
        Log.i("haha", c2.time.toString())

        c2.add(Calendar.MONTH, 1)
        c2.add(Calendar.SECOND, -1)
        Log.i("haha", c2.timeInMillis.toString())
        Log.i("haha", c2.time.toString())
        val test = TestFragment()
        var a = false
        val button: Button = findViewById(R.id.buttontest)
//        val ft = supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragment_container, test)
//        }
//        ft.commit()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, test)
            .commit()
        button.setOnClickListener {
            if (a) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, test)
                    .commit()
                a = false
            } else {
                supportFragmentManager.beginTransaction()
                    .remove(test)
                    .commit()
                a = true
            }

        }

    }

    inner class Data(var x: Float, var y: Float) {
    }
}
