package com.capsane.example.homepage

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin

open class RadarView : View {
    var count: Int = 6
        set(value) {
            angle = Math.PI * 2 / count
            field = value
        }
    private var angle = Math.PI * 2 / count
    private var radius: Float = 0f
    private var centerX = 0f
    private var centerY = 0f
    var titles = mutableListOf("天地", "日月", "风雨", "山海", "江河", "湖泊")
    var data = mutableListOf(90, 60, 60, 60, 80, 70)
    var maxValue = 100f
    private var mainPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.DKGRAY
        style = Paint.Style.STROKE
        strokeWidth = 6f
    }
    private var valuePaint: Paint = Paint().apply {
        color = Color.rgb(173, 216, 230)
        style = Paint.Style.FILL_AND_STROKE
    }
    private var fontHeight = 0f
    private var textPaint: Paint = Paint().apply {
        textSize = 30f
        color = Color.BLACK
    }

    companion object {
        val RADIUS: Float = 50f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = kotlin.math.min(h, w) / 2 * 0.9f
        centerX = w / 2f
        centerY = h / 2f
        postInvalidate()
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private var currentPoint: Point? = null

    private val centerPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLUE } }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawPolygon(it)
            drawLines(it)
            drawText(it)
            drawRegion(it)
        }
    }

    private fun drawCircle(canvas: Canvas) {
        val x = currentPoint?.x
        val y = currentPoint?.y
        canvas.drawCircle(x!!, y!!, RADIUS, centerPaint)
    }

    private fun startAnimation() {
        val startPoint = Point(RADIUS, RADIUS)
        val endPoint = Point(width - RADIUS, height - RADIUS)
        val animator: ValueAnimator = ValueAnimator.ofObject(PointEvaluator(), startPoint, endPoint)
        animator.addUpdateListener { animation ->
            currentPoint = animation.animatedValue as Point
            invalidate()
        }
        animator.duration = 5000
        animator.start()
    }

    // 方向：x正方向永远穿越顶点
    private fun drawPolygon(canvas: Canvas) {
        val path = Path()
        val r = radius / (count - 1)
        for (i in 1 until count) {
            val curR = r * i
            path.reset()        // FIXME: Clear any lines and curves from the path, making it empty.
            for (j in 0 until count) {
                if (j == 0) {
                    path.moveTo(centerX + curR, centerY)
                } else {
                    val x = centerX + curR * cos(angle * j)
                    val y = centerY + curR * sin(angle * j)
                    path.lineTo(x.toFloat(), y.toFloat())
                }
            }
            path.close()
            canvas.drawPath(path, mainPaint)
        }
    }

    private fun drawLines(canvas: Canvas) {
        val path = Path()
        for (i in 0 until count) {
            path.reset()
            path.moveTo(centerX, centerY)
            path.lineTo(centerX + radius * cos(angle * i).toFloat(), centerY + radius * sin(angle * i).toFloat())
            canvas.drawPath(path, mainPaint)
        }
    }

    private fun drawText(canvas: Canvas) {
        if (count > titles.size) {
            return
        }
        fontHeight = textPaint.fontMetrics.descent - textPaint.fontMetrics.ascent
        for (i in 0 until count) {
            val x = centerX + (radius + fontHeight) * cos(angle * i).toFloat()
            val y = centerY + (radius + fontHeight) * sin(angle * i).toFloat()
            canvas.drawText(titles[i], x, y, textPaint)
        }

    }

    private fun drawRegion(canvas: Canvas) {
        if (count > data.size) {
            return
        }
        val path = Path()
        for (i in 0 until count) {
            val curR = data[i] / maxValue * radius
            val x = centerX + curR * cos(angle * i).toFloat()
            val y = centerY + curR * sin(angle * i).toFloat()
            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
            canvas.drawCircle(x, y, 10f, valuePaint)
        }
        path.close()
        valuePaint.alpha = 220
        canvas.drawPath(path, valuePaint)
    }

    // 方向：底部的横线永远水平
    private fun drawPolygon2(canvas: Canvas) {
        val r = radius / (count - 1)
        for (i in 1 until count) {
            val curR = r * i
            for (j in 0 until count) {
                val x = curR * sin(angle / 2).toFloat()
                val y = curR * cos(angle / 2).toFloat()
                canvas.drawLine(centerX - x, centerY + y, centerX + x, centerY + y, mainPaint)
                canvas.rotate(360f / count, centerX, centerY)
            }
        }
    }

}