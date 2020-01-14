package com.capsane.example.homepage.main

import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.widget.TextView
import org.jetbrains.anko.dip
import org.jetbrains.anko.singleLine

/**
 * 自动循环滚动TextView
 */
class RollingTextView : TextView {

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
        singleLine = true
        marqueeRepeatLimit = -1
    }




//    override fun setFocusable(focusable: Int) {
//        super.setFocusable(true)
//    }
//
//    override fun setFocusable(focusable: Boolean) {
//        super.setFocusable(true)
//    }
//
//    override fun setFocusableInTouchMode(focusableInTouchMode: Boolean) {
//        super.setFocusableInTouchMode(true)
//    }
//
//    override fun isFocused(): Boolean {
//        return true
//    }
//
//    override fun hasFocus(): Boolean {
//        return true
//    }
//
//    override fun setNextFocusForwardId(nextFocusForwardId: Int) {
//        Log.e("hhh", "setNextFocusForwardId$nextFocusForwardId")
//        super.setNextFocusForwardId(nextFocusForwardId)
//    }
//
//    override fun hasExplicitFocusable(): Boolean {
//        return true
//    }
//
//    override fun setSelected(selected: Boolean) {
//        Log.e("hhh", "setSelected$selected")
//        super.setSelected(true)
//    }
//
//    override fun restoreDefaultFocus(): Boolean {
//        Log.e("hhh", "restoreDefaultFocus")
//        return super.restoreDefaultFocus()
//    }
//
//    override fun clearFocus() {
//        Log.e("hhh", "clearFocus")
////        super.clearFocus()
//    }
//
    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        Log.e("hhh", "onFocusChanged:$focused, $direction")
//        if(focused) {
//            super.onFocusChanged(focused, direction, previouslyFocusedRect)
//        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        Log.e("hhh", "onWindowFocusChanged:$hasWindowFocus")
//        if(hasWindowFocus) {
//            super.onWindowFocusChanged(hasWindowFocus)
//        }
    }
}