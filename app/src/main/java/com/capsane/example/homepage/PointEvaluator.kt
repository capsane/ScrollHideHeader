package com.capsane.example.homepage

import android.animation.TypeEvaluator

class PointEvaluator: TypeEvaluator<Any> {
    override fun evaluate(fraction: Float, startValue: Any?, endValue: Any?): Any {
        val startPoint = startValue as Point
        val endPoint = endValue as Point
        val x = startPoint.x + fraction * (endPoint.x - startPoint.x)
        val y = startPoint.y + fraction * (endPoint.y - startPoint.y)
        return Point(x, y)
    }
}