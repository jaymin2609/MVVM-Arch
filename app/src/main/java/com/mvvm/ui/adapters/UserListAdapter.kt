package com.mvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.databinding.RowUserListBinding
import com.mvvm.database.entity.UserEntity


class UserListAdapter(private val clickListener: (UserEntity) -> Unit) :
    RecyclerView.Adapter<UserListAdapter.OrderViewHolder>() {
    private val TAG = UserListAdapter::class.java.simpleName
    private val list = ArrayList<UserEntity>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): OrderViewHolder {
        val binding =
            RowUserListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(list[position], position)
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setList(subscribers: List<UserEntity>) {
        list.clear()
        list.addAll(subscribers)

    }

    override fun getItemCount(): Int = if (list.isNotEmpty()) list.size else 0

    inner class OrderViewHolder(val binding: RowUserListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserEntity, pos: Int) {
            with(binding) {
                this.item = item
                this.root.setOnClickListener { clickListener(item) }
            }
        }
    }
}
