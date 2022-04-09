package com.example.codingtest.ui
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codingtest.R

class PayloadItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var time: TextView = itemView.findViewById(R.id.time)
    var value: TextView = itemView.findViewById(R.id.value)
    var amount: TextView = itemView.findViewById(R.id.amount)
}