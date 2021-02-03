package com.example.mfm_2

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.mfm_2.`try`.Test03View
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
        val test03View: Test03View = findViewById(R.id.textView)
        test03View.text = "300.00"
        test03View.piePercent = 70
        test03View.bgColor = ContextCompat.getColor(applicationContext, R.color.gYellow)
    }

}
