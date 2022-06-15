package com.example.invscan.data.api

import com.example.invscan.domain.enteties.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
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

    @POST("items")
    fun changeItemClassroom(@Query("inventory_num") invItemNum: String, @Query("class_room_num") numClass: String):Call<StatusResponse>

}