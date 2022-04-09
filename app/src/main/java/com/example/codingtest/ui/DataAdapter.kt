package com.example.codingtest.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.codingtest.Payload
import com.example.codingtest.R
import com.example.codingtest.contextRef
import java.text.SimpleDateFormat
import java.util.*


class DataAdapter(private val data: Any?): Adapter<RecyclerView.ViewHolder>() {
    private lateinit var context : Context
    private val simpleTimeFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
    private var dataList = data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.context = parent.context
        return PayloadItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_one_line_list, parent, false)
                )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Payload = (dataList as List<*>)[position] as Payload
        if (holder is PayloadItemHolder) {
            holder.time.text = getSimpleTimeFormat(item._T)
            holder.value.text = getMoneyCommaString(item.p.toDouble())
            holder.value.setTextColor(
                if (item.m)
                    contextRef?.get()?.getColorStateList(android.R.color.holo_red_dark)
                else
                    contextRef?.get()?.getColorStateList(android.R.color.holo_green_light)
            )
            holder.amount.text = item.q
        }
    }

    override fun getItemCount(): Int {
        return (dataList as List<*>).size
    }

    private fun getMoneyCommaString(money: Double?): String? {
        return if (money == null) "-" else String.format(
            Locale.CHINESE, "%,.02f",
            money
        )
    }

    private fun getSimpleTimeFormat(now: Long): String {
        simpleTimeFormat.timeZone = TimeZone.getDefault()
        return simpleTimeFormat.format(now)
    }

    fun setData(data: Any?) {
        dataList = data
    }
}
