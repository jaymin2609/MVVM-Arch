package com.mvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.databinding.RowInspectionStageBinding
import com.mvvm.pojos.Stage


class InspectionStageAdapter(private val clickListener: (Stage) -> Unit) :
    BaseAdapter<InspectionStageAdapter.OrderViewHolder, Stage>() {
    private val classTag = InspectionStageAdapter::class.java.simpleName

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
