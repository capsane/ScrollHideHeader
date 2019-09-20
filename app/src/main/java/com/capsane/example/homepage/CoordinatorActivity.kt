package com.capsane.example.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_coordinator.*

class CoordinatorActivity : AppCompatActivity() {

    private var appleList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coordinator)

        initData()
        recyclerView?.adapter = MyAdapter()
    }

    private fun initData() {
        for (i in 0 until 20) {
            appleList.add("Apple $i")
        }
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