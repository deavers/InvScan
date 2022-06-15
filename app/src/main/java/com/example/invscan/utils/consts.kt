package com.example.invscan.utils

import com.example.invscan.R
import com.example.invscan.domain.enteties.InvItemChecked

fun getImgIdByCategory(categoryId:Int):Int{
    return when(categoryId){
        1 -> R.drawable.monitor
        2-> R.drawable.mfu
        3-> R.drawable.dprinter
        4-> R.drawable.condicioner
        5->R.drawable.closet
        6->R.drawable.table
        else ->R.drawable.chair
    }
}

fun getNameByCategory(categoryId:Int):String{
    return when(categoryId){
        1 -> "Мониторы"
        2-> "МФУ"
        3-> "Принтеры"
        4-> "Кондиционеры"
        5->"Шкафы"
        6->"Столы"
        else ->"Стулья"
    }
}

fun getItemsNames(categoryId: Int,itemsChecked:ArrayList<InvItemChecked>):String{
    val res = ""
    itemsChecked.filter{ it.item.category_id == categoryId }.forEach { itemChecked ->
        val num = itemChecked.item.inventory_num
        if (itemChecked.checked){
            num.plus("✓")
        } else {
            num.plus("✕")
        }
        num.plus("\n")
        res.plus(num)
    }
     return res
}