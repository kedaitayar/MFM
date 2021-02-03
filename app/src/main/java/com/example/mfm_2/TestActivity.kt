package com.example.mfm_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.mfm_2.ui.customview.PillPieCustomView

/**
 * The purpose of this activity is for testing component or code before implementing it to the app
 */

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val pillPieCustomView: PillPieCustomView = findViewById(R.id.textView)
        pillPieCustomView.text = "300.00"
        pillPieCustomView.piePercent = 70
        pillPieCustomView.bgColor = ContextCompat.getColor(applicationContext, R.color.gYellow)
    }

}
