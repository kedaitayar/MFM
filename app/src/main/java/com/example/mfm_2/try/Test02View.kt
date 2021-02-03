package com.example.mfm_2.`try`

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.fonts.FontFamily
import android.graphics.fonts.FontStyle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mfm_2.R
import com.example.mfm_2.util.DpConverter.toPx

class Test02View @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private var radius = 0f
    private var text: CharSequence = ""
    private var mTextSize = 0f
    private var textWidth = 0
    private var piePercent = 0

    init {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.Test02View)
        if (isInEditMode) {
            text = "Text"
            mTextSize = 24.toPx().toFloat()
            piePercent = 80
        } else {
            text = typeArray.getString(R.styleable.Test02View_android_text).toString()
            mTextSize = typeArray.getFloat(R.styleable.Test02View_android_textSize, 24.toPx().toFloat())
            piePercent = typeArray.getInt(R.styleable.Test02View_piePercent, 100)
        }
        radius = mTextSize * 0.6f
        textWidth = paint.apply { textSize = mTextSize }.measureText(text.toString()).toInt()
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawStuff(canvas)
    }

    private fun drawStuff(canvas: Canvas?) {
        canvas?.save()
        paint.apply {
            strokeWidth = 4f
            color = ContextCompat.getColor(context, R.color.gGreen)
            style = Paint.Style.FILL
        }
        path.rewind()
        path.addCircle(radius, radius, radius * 0.85f, Path.Direction.CCW)
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            canvas?.clipPath(path, Region.Op.DIFFERENCE)
        } else {
            canvas?.clipOutPath(path)
        }

        canvas?.drawCircle(radius, radius, radius, paint)
        canvas?.drawRect(radius, 0f, radius * 2, radius * 2, paint)
        val rect01 = RectF(radius, 0f, width.toFloat(), radius * 2)
        canvas?.drawRoundRect(rect01, radius * 0.7f, radius * 0.7f, paint)
        canvas?.restore()

        paint.color = Color.GRAY
        var diameter = radius * 2
        val rect02 = RectF(diameter * 0.15f, diameter * 0.15f, diameter * 0.85f, diameter * 0.85f)
        canvas?.drawArc(rect02, -90f, piePercent.toPercentage(), true, paint)

        paint.apply {
            color = Color.BLACK
            textSize = mTextSize
            textAlign = Paint.Align.LEFT
//            typeface = Typeface.DEFAULT_BOLD
        }

        textWidth = paint.measureText(text.toString()).toInt()
        canvas?.drawText(text.toString(), radius * 2f, (height / 2) - (paint.descent() + paint.ascent()) / 2, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension((radius * 2.4f + textWidth).toInt(), (radius * 2).toInt())
    }

    fun setText(string: String) {
        text = string
        invalidate()
    }

    private fun Int.toPercentage(): Float = this * 360 * 0.01f
}