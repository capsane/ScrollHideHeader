package com.capsane.example.homepage.main

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capsane.example.homepage.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.dip

class MainActivity : AppCompatActivity() {

    private var announcement = "123123123123123123123123123123123123123123123123123123"
        set(value) {
            if (field == value) {
                return
            }
            field = value
            view?.setContent("一二三万i我厉害你骄傲给你哦爱狗弄哦i挤公交哦赶紧哦i就给你哦i工")
        }

    private var list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button?.setOnClickListener {

        }


        rolling_text_view?.isSelected = true
        initData()
        rv?.layoutManager = LinearLayoutManager(this@MainActivity)
        rv?.adapter = MyAdapter()
        nested_scroll_view?.onScrollChangedCallback = { percent ->
            if (percent < 0.8f) {
                search_bar?.visibility = View.VISIBLE
                search_bar?.alpha = 1 - (percent / 0.8f)
            } else {
                search_bar?.visibility = View.INVISIBLE
            }
        }

    }

    private fun initData() {
        for (i in 0 until 20) {
            list.add(i.toString())
        }
    }

    inner class MyAdapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(this@MainActivity).inflate(
                    R.layout.string_item,
                    parent,
                    false
                ) as LinearLayout
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.v.findViewById<TextView>(R.id.item_tv).text = list[position]
        }
    }

    inner class ViewHolder(val v: LinearLayout) : RecyclerView.ViewHolder(v)




    /**
     * 计算消息弹幕的宽度
     */
    private fun calculateInfoBulletWidth(message: String): Float {
        val paint = Paint().apply {
            textSize = dip(12).toFloat()
        }
        val textLength = paint.measureText(message)

        return textLength + dip(33)
    }
}
