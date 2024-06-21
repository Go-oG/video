package com.goog.videodemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.goog.videodemo.R
import com.goog.videodemo.data.FilterItem

class FilterAdapter(val context: Context) : Adapter<FilterHolder>() {
    val dataList = mutableListOf<FilterItem>()
    var clickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false)
        val holder = FilterHolder(view)
        view.setOnClickListener {
            clickListener?.invoke(holder.adapterPosition)
        }
        return holder
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: FilterHolder, position: Int) {
        val data = dataList[position]
        holder.indexTV.text = "$position"
        holder.nameTv.text = dataList[position].name
        if (data.select) {
            holder.root.setBackgroundColor(0xFF2196F3.toInt())
        } else {
            holder.root.setBackgroundColor(0xFFFFFFFF.toInt())
        }
    }
}

class FilterHolder(view: View) : RecyclerView.ViewHolder(view) {
    internal val nameTv: TextView = view.findViewById(R.id.nameTV)
    internal val root: CardView = view.findViewById(R.id.itemRoot)
    internal val indexTV = view.findViewById<TextView>(R.id.indexTV)

}