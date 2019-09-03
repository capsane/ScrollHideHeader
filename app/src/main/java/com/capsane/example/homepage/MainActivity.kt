package com.capsane.example.homepage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var list = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
}
