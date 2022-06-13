package com.example.invscan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.invscan.R
import com.example.invscan.databinding.InvItemLayoutBinding
import com.example.invscan.domain.enteties.InvItemChecked
import com.example.invscan.domain.enteties.InventoryItem
import com.example.invscan.interfaces.OnInvItemListener
import com.example.invscan.utils.getImgIdByCategory

class InvItemAdapter:ListAdapter<InvItemChecked,InvItemAdapter.InvViewHolder>(DiffInvItemCallback()){

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

/*    override fun onViewRecycled(holder: InvViewHolder) {
        super.onViewRecycled(holder)
        holder.binding.apply {
            switchCheck.isChecked = false
            tvInvNum.text = ""
        }
    }*/

    override fun onBindViewHolder(holder: InvViewHolder, position: Int) {
        val invItem = getItem(position)
        holder.binding.apply {
            imgCategory.setImageResource(getImgIdByCategory(invItem.item.category_id))
            switchCheck.setOnClickListener {
                onItemCheckListener?.onInvItemClick(invItem.item,switchCheck.isChecked)
            }
            switchCheck.isChecked = invItem.checked
            tvInvNum.text = invItem.item.inventory_num
        }
    }


}