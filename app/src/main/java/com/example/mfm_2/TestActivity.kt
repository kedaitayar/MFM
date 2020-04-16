package com.example.mfm_2

import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import com.github.mikephil.charting.charts.Chart.PAINT_GRID_BACKGROUND
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlin.random.Random

/**
 * The purpose of this activity is for testing component or code before implementing it to the app
 */

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val chart: LineChart = findViewById(R.id.chart1)
//        Log.i("test", "hoho")
        var entries: MutableList<Entry> = mutableListOf()
        var data = listOf<Data>(Data(1f,1f), Data(2f,2f), Data(3f,3f), Data(4f,2f), Data(5f,1f))
        var data2 = arrayOf<Array<Int>>(arrayOf(1,2,3,4,5), arrayOf(1,2,3,4,5))
        var data3 = arrayOf(1 ,9 ,3 ,9 ,10 ,4 ,3 ,5 ,9 ,6 ,1 ,6 ,7 ,9 ,2 ,10 ,5 ,2 ,5 ,5 ,2 ,2 ,5 ,8 ,5 ,1 ,7 ,2 ,10 ,4 ,1 ,9 ,3 ,3 ,10 ,5 ,9 ,8 ,8 ,3 ,4 ,9 ,7 ,3 ,10 ,9 ,5 ,7 ,9 ,4 ,6 ,10 ,9 ,6 ,5 ,4 ,8 ,3 ,4 ,3 ,6 ,2 ,7 ,4 ,7 ,1 ,7 ,10 ,8 ,2 ,10 ,4 ,6 ,7 ,8 ,9 ,1 ,2 ,1 ,7 ,1 ,6 ,10 ,1 ,1 ,9 ,8 ,3 ,6 ,3 ,10 ,9 ,5 ,4 ,10 ,9 ,10 ,9 ,5 ,8 )
        var data4 = arrayOf(2 ,1 ,2 ,2 ,1 ,1 ,2 ,1 ,2 ,1 ,1 ,1 ,2 ,2 ,2 ,2 ,2 ,1 ,2 ,1 ,2 ,1 ,2 ,2 ,2 ,1 ,2 ,1 ,1 ,1 ,2 ,1 ,1 ,2 ,1 ,2 ,1 ,2 ,2 ,1 ,1 ,2 ,2 ,1 ,2 ,2 ,2 ,1 ,1 ,1 ,2 ,2 ,1 ,1 ,2 ,1 ,1 ,2 ,1 ,1 ,2 ,2 ,1 ,2 ,1 ,1 ,1 ,2 ,2 ,2 ,2 ,2 ,1 ,1 ,1 ,2 ,1 ,1 ,2 ,2 ,1 ,1 ,1 ,2 ,1 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,2 ,1 ,2 ,1 ,1 ,2 ,2 ,2 )
        var data5 = mutableListOf<Int>()
        var data6 = mutableListOf<Int>()
        var total = 50
        var i = 0
        while (i<30){
            if(data4[i] == 2){
                total -= data3[i]
            } else {
                total += data3[i]
            }
            data5.add(i)
            data6.add(total)
            entries.add(Entry(i.toFloat(), total.toFloat()))
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


        var lineData: LineData = LineData(dataSet)
        lineData.setDrawValues(false)

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

        //progress bar//
        val progressbar: ProgressBar = findViewById(R.id.progressBar)
        progressbar.progress = 75
    }

    inner class Data(var x: Float, var y: Float){
    }

}
