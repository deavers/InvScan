package com.example.invscan.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.invscan.domain.enteties.InventoryItem

class DiffInvItemCallback:DiffUtil.ItemCallback<InventoryItem>() {
    override fun areItemsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
        return oldItem.inventory_num == newItem.inventory_num
    }
    override fun areContentsTheSame(oldItem: InventoryItem, newItem: InventoryItem): Boolean {
        return oldItem == newItem
    }
}