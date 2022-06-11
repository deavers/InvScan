package com.example.invscan.domain.enteties

data class InventoryItem(
    val inventory_num:String,
    val name:String,
    val category_id:Int,
    val class_room_num:String,
)

data class GetItemResponse(val status:Boolean,val message:String,val item: InventoryItem)