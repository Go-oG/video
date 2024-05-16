package com.goog.videodemo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar

@SuppressLint("NotifyDataSetChanged")
class SeekAdapter(val context: Context, private var filterItem: FilterItem?) : RecyclerView.Adapter<ViewHolder>() {
    private var dataList: List<Parameter> = emptyList()

    init {
        changeFilter(filterItem)
    }


    fun changeFilter(item: FilterItem?) {
        if (item == null) {
            filterItem = null
            dataList = listOf()
            notifyDataSetChanged()
            return
        }
        this.filterItem = item
        dataList = item.builder.getParameters()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (dataList.isEmpty()) {
            return 1
        }
        return 2
    }

    override fun getItemCount(): Int {
        if (dataList.isEmpty()) {
            return 1
        }
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == 1) {
            return EmptyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_empty, parent, false))
        }
        val holder = SeekViewHolder(LayoutInflater.from(context).inflate(R.layout.item_seek, parent, false))


        return holder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder !is SeekViewHolder) {
            return
        }
        val data = dataList[position]
        holder.seekBar.setRange(data.minValue, data.maxValue,data.step)
        holder.seekBar.setProgress(data.curValue)
        val s = "[${data.minValue.format()},${data.maxValue.format()}],cur:${data.curValue.format()}"
        holder.title.text = "${data.name} $s"
    }

    inner class SeekViewHolder(view: View) : ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.nameTV)
        val seekBar: RangeSeekBar = view.findViewById(R.id.seekBar)

        init {
            seekBar.setOnRangeChangedListener(object : OnRangeChangedListener {
                override fun onRangeChanged(view: RangeSeekBar, leftValue: Float, rightValue: Float,
                    isFromUser: Boolean) {
                    if (!isFromUser) {
                        return
                    }

                    val item = filterItem ?: return
                    val filter = item.filter ?: return

                    val data = dataList[adapterPosition]
                    Log.i("SeekAdapter", "L:${leftValue.format()} R:${rightValue.format()}")
                    data.curValue = leftValue
                    item.builder.changeParameter(filter, data.index, leftValue)
                    notifyItemChanged(adapterPosition)
                }

                override fun onStartTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {

                }

                override fun onStopTrackingTouch(view: RangeSeekBar, isLeft: Boolean) {

                }
            })
        }
    }
}

class EmptyViewHolder(view: View) : ViewHolder(view)

@SuppressLint("DefaultLocale")
private fun Float.format(): String {
    return String.format("%.2f", this)
}