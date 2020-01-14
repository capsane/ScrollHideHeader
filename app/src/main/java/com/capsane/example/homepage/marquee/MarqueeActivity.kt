package com.capsane.example.homepage.marquee

import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capsane.example.homepage.R
import kotlinx.android.synthetic.main.activity_marquee.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.wrapContent


const val MAX_NOTICE_WIDTH = 93
const val MARQUEE_SPEED_PER_PIXEL = 30f
const val MARQUEE_TEXT_SIZE = 10

class MarqueeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marquee)

        button?.setOnClickListener {
            val text = edit_text?.text?.toString()
            val announcement = text ?: "一二三四五六七八九十1234567890"
            initMarquee(announcement)
        }
    }

    private fun initMarquee(text: String) {
        val enableMarquee = calculateTextWidth(text, MARQUEE_TEXT_SIZE) > dip(MAX_NOTICE_WIDTH)
        rv_marquee?.adapter = MarqueeAdapter(mutableListOf(text), enableMarquee)
        rv_marquee?.layoutParams?.width = if (enableMarquee) dip(MAX_NOTICE_WIDTH) else wrapContent
        rv_marquee?.layoutManager = MarqueeLayoutManager(this@MarqueeActivity, LinearLayoutManager.HORIZONTAL, false, MARQUEE_SPEED_PER_PIXEL)
        // remove all item decorations
        val itemDecorationCount = rv_marquee?.itemDecorationCount ?: 0
        for (i in 0 until itemDecorationCount) {
            rv_marquee?.removeItemDecorationAt(i)
        }

        if (enableMarquee) {
            rv_marquee?.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    super.getItemOffsets(outRect, view, parent, state)
                    outRect.left = dip(MAX_NOTICE_WIDTH / 2)
                }
            })
            rv_marquee?.smoothScrollToPosition(rv_marquee?.adapter?.itemCount ?: 0)
        }
    }

    /**
     * 计算文字宽度
     */
    private fun calculateTextWidth(text: String, textSizeDp: Int): Float {
        val paint = Paint().apply {
            textSize = dip(textSizeDp).toFloat()
        }
        return paint.measureText(text)
    }
}
