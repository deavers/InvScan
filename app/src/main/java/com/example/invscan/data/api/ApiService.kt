package com.example.invscan.data.api

import com.example.invscan.domain.enteties.GetAllClassroomsResponse
import com.example.invscan.domain.enteties.GetClassroomResponse
import com.example.invscan.domain.enteties.GetItemResponse
import com.example.invscan.domain.enteties.GetItemsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("classrooms")
    fun getAllClassrooms():Call<GetAllClassroomsResponse>

    @GET("classroom")
    fun getClassroomByNum(@Query("num") num:String):Call<GetClassroomResponse>

    @GET("item")
    fun getItemByNum(@Query("in_num") num: String):Call<GetItemResponse>

    @GET("items")
    fun getItems():Call<GetItemsResponse>

}