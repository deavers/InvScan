package com.example.invscan.domain.repository

import com.example.invscan.domain.enteties.Classroom
import com.example.invscan.domain.enteties.GetAllClassroomsResponse
import com.example.invscan.domain.enteties.GetClassroomResponse
import retrofit2.Call

interface DataRepository {
    fun getAllClassrooms(): Call<GetAllClassroomsResponse>
    fun getItemsByClassroomNum(num:String):Call<GetClassroomResponse>
}