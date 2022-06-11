package com.example.invscan.data.api

import com.example.invscan.domain.enteties.GetAllClassroomsResponse
import com.example.invscan.domain.enteties.GetClassroomResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("classrooms")
    fun getAllClassrooms():Call<GetAllClassroomsResponse>

    @GET("classroom")
    fun getClassroomByNum(@Query("num") num:String):Call<GetClassroomResponse>

}