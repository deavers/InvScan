package com.example.invscan.utils

import com.example.invscan.R

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