package com.goog.videodemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter


class RVAdapter(val context: Context) : Adapter<RVHolder>() {
    val dataList = mutableListOf<FilterItem>()
    var clickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false)
        val holder = RVHolder(view)
        view.setOnClickListener {
            clickListener?.invoke(holder.adapterPosition)
        }
        return holder
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: RVHolder, position: Int) {
        holder.nameTv.text = dataList[position].name
    }

}

class RVHolder(view: View) : RecyclerView.ViewHolder(view) {
    internal val nameTv: TextView = view.findViewById(R.id.nameTV)

}