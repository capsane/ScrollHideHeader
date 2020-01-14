package com.capsane.example.homepage.marquee

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class MarqueeRecyclerView : RecyclerView {

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        setupView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        setupView()
    }

    private fun setupView() {
        setOnScrollListener(object: OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // 只有滑动到target时才会IDLE
                if (newState == SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as? MarqueeLayoutManager
                    layoutManager?.let {
                        val position = it.findLastVisibleItemPosition()
                        val itemCount = layoutManager.itemCount
                        if (position == itemCount - 1) {
                            layoutManager.scrollToPosition(0)
                            this@MarqueeRecyclerView.smoothScrollToPosition(itemCount)
                        }
                    }
                }
            }
        })
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return true
    }
}