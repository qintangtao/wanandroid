package me.tang.wanandroid.adapter

import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter

class LoggingRecyclerViewAdapter<T> : BindingRecyclerViewAdapter<T>() {


    override fun onCreateViewHolder(binding: ViewDataBinding): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(binding).apply {
            Log.d("LoggingRecyclerViewAdapter", "onCreateViewHolder: $this")
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        Log.d("LoggingRecyclerViewAdapter", "onBindViewHolder: holder:$holder, position:$position, payloads:${payloads.size}")
    }
}