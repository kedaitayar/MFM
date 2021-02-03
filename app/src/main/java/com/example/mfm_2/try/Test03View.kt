package com.example.mfm_2.`try`

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.mfm_2.R

class Test03View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
    private val TAG: String? = "hoho"
    private val path = Path()
    private var radius = 0f
    var piePercent: Int = 0
        set(value) {
            field = value
            invalidate()
        }
    var bgColor = 0
        set(value) {
            field = value
            invalidate()
        }

    init {
        this.setPadding(paddingLeft, paddingTop, paddingRight + (textSize*1.75).toInt(), paddingBottom)
        bgColor = ContextCompat.getColor(context, R.color.gGreen)
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.Test03View)
        if (isInEditMode) {
            this.text = "Test"
            piePercent = 80
        } else {
            piePercent = typeArray.getInt(R.styleable.Test03View_piePercentage, 0)
        }
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        Log.i(TAG, "onDraw called")
        drawPieAndBG(canvas)
        canvas?.save()
        canvas?.translate(height.toFloat(), height * -0.02f)
        super.onDraw(canvas)
        canvas?.restore()
    }

    private fun drawPieAndBG(canvas: Canvas?) {
        canvas?.save()
        paint.apply {
            strokeWidth = 4f
            color = this@Test03View.bgColor
            style = Paint.Style.FILL
        }
        radius = height / 2f
        path.rewind()
        path.addCircle(radius, radius, radius * 0.75f, Path.Direction.CCW)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            canvas?.clipPath(path, Region.Op.DIFFERENCE)
        } else {
            canvas?.clipOutPath(path)
        }

        canvas?.drawCircle(radius, radius, radius, paint)
        canvas?.drawRect(radius, 0f, height.toFloat(), height.toFloat(), paint)
        val rect01 = RectF(radius, 0f, width.toFloat(), height.toFloat())
        canvas?.drawRoundRect(rect01, radius * 0.7f, radius * 0.7f, paint)
        canvas?.restore()

        paint.color = Color.GRAY
        val rect02 = RectF(height * 0.2f, height * 0.2f, height * 0.8f, height * 0.8f)
        canvas?.drawArc(rect02, -90f, piePercent.toPiePercentage(), true, paint)
    }

    private fun Int.toPiePercentage(): Float = this * 360 * 0.01f
}