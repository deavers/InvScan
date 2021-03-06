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

fun getItemName(itemChecked:InvItemChecked):String{
    var res = ""
    var num = itemChecked.item.inventory_num
    if (itemChecked.checked){
        num+=(" ✓")
    } else {
        num+=(" ✕")
    }
    num+="\n"
    res+=num
    return res
}