package com.example.invscan.data.repository

import com.example.invscan.data.api.RetrofitInstance
import com.example.invscan.domain.enteties.*
import com.example.invscan.domain.repository.DataRepository
import retrofit2.Call

class RepositoryImpl:DataRepository {


    override fun getAllClassrooms(): Call<GetAllClassroomsResponse> {
        return RetrofitInstance.api.getAllClassrooms()
    }

    override fun getItemsByClassroomNum(num: String): Call<GetClassroomResponse> {
        return RetrofitInstance.api.getClassroomByNum(num)
    }

    override fun getItemByNum(num: String): Call<GetItemResponse> {
       return RetrofitInstance.api.getItemByNum(num)
    }

    override fun getItems(): Call<GetItemsResponse> {
        return RetrofitInstance.api.getItems()
    }


}