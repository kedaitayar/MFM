package com.example.mfm_2.`try`

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class Test01view @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Paint object for coloring and styling
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    // Some colors for the face background, eyes and mouth.
    private var faceColor = Color.YELLOW
    private var eyesColor = Color.BLACK
    private var mouthColor = Color.BLACK
    private var borderColor = Color.BLACK

    // Face border width in pixels
    private var borderWidth = 4.0f

    // View size in pixels
    private var size = 320

    private val mouthPath = Path()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawFaceBackground(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    private fun drawMouth(canvas: Canvas?) {
        mouthPath.moveTo(size * 0.22f, size * 0.7f)
        mouthPath.quadTo(size * 0.50f, size * 0.80f, size * 0.78f, size * 0.70f)
        mouthPath.quadTo(size * 0.50f, size * 0.90f, size * 0.22f, size * 0.70f)
        paint.color = mouthColor
        paint.style = Paint.Style.FILL

        canvas?.drawPath(mouthPath, paint)

    }

    private fun drawEyes(canvas: Canvas?) {
        paint.apply {
            color = eyesColor
            style = Paint.Style.FILL
        }
        val leftEyeRectF = RectF(size * 0.32f, size * 0.23f, size * 0.43f, size * 0.5f)
        canvas?.drawOval(leftEyeRectF, paint)

        val rightEyeRectF = RectF(size * 0.57f, size * 0.23f, size * 0.68f, size * 0.5f)
        canvas?.drawOval(rightEyeRectF, paint)
    }

    private fun drawFaceBackground(canvas: Canvas?) {
        paint.apply {
            color = faceColor
            style = Paint.Style.FILL
        }
        val radius = size / 2f
        canvas?.drawCircle(size / 2f, size / 2f, radius, paint)

        paint.apply {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }
}