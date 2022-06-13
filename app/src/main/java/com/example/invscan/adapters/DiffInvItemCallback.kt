package com.example.invscan.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.invscan.domain.enteties.InvItemChecked

class DiffInvItemCallback:DiffUtil.ItemCallback<InvItemChecked>() {
    override fun areItemsTheSame(oldItem: InvItemChecked, newItem: InvItemChecked): Boolean {
        return oldItem.item.inventory_num == newItem.item.inventory_num
    }
    override fun areContentsTheSame(oldItem: InvItemChecked, newItem: InvItemChecked): Boolean {
        return oldItem.item == newItem.item
    }
}