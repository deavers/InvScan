package com.example.invscan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.invscan.R
import com.example.invscan.databinding.InvItemLayoutBinding
import com.example.invscan.domain.enteties.InventoryItem
import com.example.invscan.interfaces.OnInvItemListener
import com.example.invscan.utils.getImgIdByCategory

class InvItemAdapter:ListAdapter<InventoryItem,InvItemAdapter.InvViewHolder>(DiffInvItemCallback()){

    var onItemCheckListener:OnInvItemListener? = null

    class InvViewHolder(val binding: InvItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvViewHolder {
        val binding = InvItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InvViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvViewHolder, position: Int) {
        val invItem = getItem(position)
        holder.binding.apply {
            imgCategory.setImageResource(getImgIdByCategory(invItem.category_id))
            switchCheck.setOnClickListener {
                onItemCheckListener?.onInvItemClick(invItem,switchCheck.isChecked)
            }
            tvInvNum.text = invItem.inventory_num
        }
    }


}