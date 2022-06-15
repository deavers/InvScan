package com.example.invscan.domain.enteties

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InventoryItem(
    val inventory_num:String,
    val name:String,
    val category_id:Int,
    val class_room_num:String,
):Parcelable

@Parcelize
data class InvItemChecked(val item: InventoryItem,var checked:Boolean): Parcelable

data class GetItemResponse(val status:Boolean,val message:String,val item: InventoryItem)

data class GetItemsResponse(val status:Boolean,val message:String,val items: List<InventoryItem>?)