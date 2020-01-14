package com.capsane.example.homepage.marquee

import android.content.Context
import android.graphics.PointF
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class MarqueeLayoutManager : LinearLayoutManager {

    private var linearSmoothScroller: MyLinearSmoothScroller? = null

    private var speedPerPixel: Float? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean, speedPerPixel: Float? = 15f) : super(context, orientation, reverseLayout) {
        this.speedPerPixel = speedPerPixel
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        if (linearSmoothScroller == null) {
            linearSmoothScroller = MyLinearSmoothScroller(recyclerView!!.context)
        }
        linearSmoothScroller?.targetPosition = position
        startSmoothScroll(linearSmoothScroller)
    }

    inner class MyLinearSmoothScroller(context: Context) : LinearSmoothScroller(context) {

        /**
         * 禁止使用DecelerateInterpolator
         */
        override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {

        }

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@MarqueeLayoutManager.computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return (speedPerPixel ?: 15f) / displayMetrics!!.density
        }
    }
}