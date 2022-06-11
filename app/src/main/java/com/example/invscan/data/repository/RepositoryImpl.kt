package com.example.invscan.data.repository

import com.example.invscan.data.api.RetrofitInstance
import com.example.invscan.domain.enteties.Classroom
import com.example.invscan.domain.enteties.GetAllClassroomsResponse
import com.example.invscan.domain.repository.DataRepository
import retrofit2.Call

class RepositoryImpl:DataRepository {


    override fun getAllClassrooms(): Call<GetAllClassroomsResponse> {
        return RetrofitInstance.api.getAllClassrooms()
    }


}