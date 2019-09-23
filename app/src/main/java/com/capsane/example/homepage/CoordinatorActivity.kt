package com.capsane.example.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_coordinator.*
import kotlin.math.abs

fun setAlphaComponent(color: Int, @IntRange(from = 0x0, to = 0xFF) alpha: Int): Int {
    if (alpha < 0 || alpha > 255) {
        throw IllegalArgumentException("alpha must be between 0 and 255.")
    }
    return (color and 0x00ffffff) or  (alpha.shl(24))
}

class CoordinatorActivity : AppCompatActivity() {

    private var appleList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)
        // RecyclerView
        initData()
        recyclerView?.adapter = MyAdapter()

        // TabLayout
        for (i in 0..2) {
            tablayout?.addTab(tablayout.newTab().setText("Tab$i"))
        }

        //
        setupView()
    }

    private fun initData() {
        for (i in 0 until 20) {
            appleList.add("Apple $i")
        }
    }

    private fun setupView() {
        top_toolbar?.background?.mutate()?.alpha = 0    // mutate()将background修改为可变
        appbar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val totalScrollRange = appbar.totalScrollRange
            val percentage = verticalOffset.toFloat() / totalScrollRange
            val absPercentage = abs(percentage)
            val alpha = (absPercentage * 255).toInt()
            // tab
            tablayout?.alpha = absPercentage
            // 顶bar背景
            top_toolbar?.background?.mutate()?.alpha = alpha
            product_detail_div?.alpha = absPercentage
            // btn_bg alpha
            back_btn?.background?.mutate()?.alpha = 255 - alpha
            share_btn?.background?.mutate()?.alpha = 255 - alpha
            // btn color&alpha
            if (absPercentage > 0.5f) {
                val color = resources.getColor(R.color.colorBlack)
                val alphaComponent = setAlphaComponent(color, alpha)
                back_btn.setColorFilter(alphaComponent)
                share_btn.setColorFilter(alphaComponent)
            } else {
                val color = resources.getColor(R.color.colorWhite)
                val alphaComponent = setAlphaComponent(color, 255 - alpha)
                back_btn.setColorFilter(alphaComponent)
                share_btn.setColorFilter(alphaComponent)
            }
        })
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(LayoutInflater.from(this@CoordinatorActivity).inflate(R.layout.fruit_item, parent, false) as FrameLayout)
        }


        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.v.findViewById<TextView>(R.id.item_text_view)?.text = appleList[position]
        }

        override fun getItemCount(): Int {
            return appleList.size
        }
    }

    class MyViewHolder(var v: FrameLayout) : RecyclerView.ViewHolder(v)

}