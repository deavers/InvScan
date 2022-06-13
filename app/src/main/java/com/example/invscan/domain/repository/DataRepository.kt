package com.example.invscan.domain.repository

import com.example.invscan.domain.enteties.Classroom
import com.example.invscan.domain.enteties.GetAllClassroomsResponse
import com.example.invscan.domain.enteties.GetClassroomResponse
import com.example.invscan.domain.enteties.GetItemResponse
import retrofit2.Call

interface DataRepository {
    fun getAllClassrooms(): Call<GetAllClassroomsResponse>
    fun getItemsByClassroomNum(num:String):Call<GetClassroomResponse>
    fun getItemByNum(num: String):Call<GetItemResponse>
}