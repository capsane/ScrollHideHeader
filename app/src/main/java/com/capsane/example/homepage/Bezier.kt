package com.capsane.example.homepage

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class Bezier : View {

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var paint: Paint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.BLACK
        textSize = 60f
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    private var path = Path()

    private var centerX: Int = 0
    private var centerY: Int = 0

    private var start: PointF = PointF(0f, 0f)
    private var end: PointF = PointF(0f, 0f)
    private var control: PointF = PointF(0f, 0f)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2

        start.x = centerX - 200f
        start.y = centerY.toFloat()

        end.x = centerX + 200f
        end.y = centerY.toFloat()

        control.x = centerX.toFloat()
        control.y = centerY - 100f
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        control.x = event?.x ?: control.x
        control.y = event?.y ?: control.y
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            // 数据点和控制点
            paint.color = Color.GRAY
            paint.strokeWidth = 20f
            canvas.drawPoint(start.x, start.y, paint)
            canvas.drawPoint(end.x, end.y, paint)
            canvas.drawPoint(control.x, control.y, paint)

            // 辅助线
            paint.strokeWidth = 4f
            canvas.drawLine(start.x, start.y, control.x, control.y, paint)
            canvas.drawLine(end.x, end.y, control.x, control.y, paint)

            // 贝塞尔曲线
            paint.color = Color.RED
            paint.strokeWidth = 8f

            path.reset()
            path.moveTo(start.x, start.y)
            path.quadTo(control.x, control.y, end.x, end.y)
            canvas.drawPath(path, paint)
        }
    }
}