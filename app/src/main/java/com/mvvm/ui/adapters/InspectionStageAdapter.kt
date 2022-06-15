package com.mvvm.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.databinding.RowInspectionStageBinding
import com.mvvm.pojos.Stage


class InspectionStageAdapter(private val clickListener: (Stage) -> Unit) :
    RecyclerView.Adapter<InspectionStageAdapter.OrderViewHolder>() {
    private val TAG = InspectionStageAdapter::class.java.simpleName
    private val list = mutableListOf<Stage>()
    private lateinit var context: Context

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            RowInspectionStageBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.context = recyclerView.context
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setList(subscribers: List<Stage>) {
        list.clear()
        list.addAll(subscribers)
    }

    override fun getItemCount(): Int = if (list.isNotEmpty()) list.size else 0

    inner class OrderViewHolder(val binding: RowInspectionStageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Stage, pos: Int) {
            with(binding) {
                this.item = item
                this.pos = pos
                this.totalCount = list.size - 1
                this.root.setOnClickListener { clickListener(item) }
            }
        }
    }
}
