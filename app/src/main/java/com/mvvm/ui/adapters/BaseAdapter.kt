package com.mvvm.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<T : RecyclerView.ViewHolder?, E> :
    RecyclerView.Adapter<T>() {
    private val classTag = BaseAdapter::class.java.simpleName
    var list = mutableListOf<E>()
        set(value) {
            list.clear()
            list.addAll(value)
        }

    lateinit var context: Context


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.context = recyclerView.context
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun getItemCount(): Int = if (list.isNotEmpty()) list.size else 0

}
