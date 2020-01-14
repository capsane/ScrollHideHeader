package com.capsane.example.homepage.marquee

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.singleLine
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent

class MarqueeAdapter(private val mDataList: MutableList<String>, private val enableMarquee: Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(wrapContent, matchParent)
            gravity = Gravity.CENTER_VERTICAL
            textSize = MARQUEE_TEXT_SIZE.toFloat()
            textColor = Color.WHITE
            singleLine = true
        }
        return Holder(textView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemView = holder.itemView as? TextView
        val message = mDataList.elementAtOrNull(position % mDataList.size)
        itemView?.let {
            it.text = message
        }
    }

    override fun getItemCount(): Int {
        return if (enableMarquee && mDataList.isNotEmpty()) Int.MAX_VALUE else mDataList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)
}