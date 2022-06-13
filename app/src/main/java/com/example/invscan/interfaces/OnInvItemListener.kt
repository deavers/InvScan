package com.example.invscan.interfaces

import com.example.invscan.domain.enteties.InventoryItem

interface OnInvItemListener {
    fun onInvItemClick(inventoryItem: InventoryItem, checked:Boolean)
}