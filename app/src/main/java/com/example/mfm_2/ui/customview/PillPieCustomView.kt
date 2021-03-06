package com.example.mfm_2.ui.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.example.mfm_2.R

class PillPieCustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr) {
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
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.PillPieCustomView)
        if (isInEditMode) {
            this.text = "Placeholder"
            piePercent = 80
        } else {
            piePercent = typeArray.getInt(R.styleable.PillPieCustomView_piePercentage, 0)
        }
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
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
            color = this@PillPieCustomView.bgColor
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