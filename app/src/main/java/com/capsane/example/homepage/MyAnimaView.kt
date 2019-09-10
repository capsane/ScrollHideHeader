package com.capsane.example.homepage

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class MyAnimaView : View {

    companion object {
        val RADIUS: Float = 50f
    }

    private var currentPoint: Point? = null

    private val centerPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.BLUE } }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
//        if (currentPoint == null) {
//            currentPoint = Point(RADIUS, RADIUS)
//            canvas?.let { drawCircle(it) }
//            startAnimation()
//        } else {
//            canvas?.let {
//                drawCircle(it)
//            }
//        }
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

}