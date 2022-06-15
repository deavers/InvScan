package com.example.invscan.domain.repository

import com.example.invscan.domain.enteties.*
import retrofit2.Call

interface DataRepository {
    fun getAllClassrooms(): Call<GetAllClassroomsResponse>
    fun getItemsByClassroomNum(num:String):Call<GetClassroomResponse>
    fun getItemByNum(num: String):Call<GetItemResponse>
    fun getItems():Call<GetItemsResponse>
    fun changeItemClassroom(invItemNum: String, numClass: String):Call<StatusResponse>
}