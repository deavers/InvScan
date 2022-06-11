package com.example.invscan.domain.repository

import com.example.invscan.domain.enteties.Classroom
import com.example.invscan.domain.enteties.GetAllClassroomsResponse
import retrofit2.Call

interface DataRepository {
    fun getAllClassrooms(): Call<GetAllClassroomsResponse>
}