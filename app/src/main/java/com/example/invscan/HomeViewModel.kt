package com.example.invscan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.invscan.data.repository.RepositoryImpl
import com.example.invscan.domain.enteties.Classroom
import com.example.invscan.domain.enteties.GetAllClassroomsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel:ViewModel() {

    private val repository = RepositoryImpl()

    private val _listClassrooms= MutableLiveData<List<Classroom>?>()
    val listClassrooms:LiveData<List<Classroom>?>
    get() = _listClassrooms

    fun getAllClassrooms(){
        repository.getAllClassrooms().enqueue(object : Callback<GetAllClassroomsResponse> {
            override fun onResponse(
                call: Call<GetAllClassroomsResponse>,
                response: Response<GetAllClassroomsResponse>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        _listClassrooms.postValue(it.classrooms)
                    }

                } else {
                    _listClassrooms.postValue(null)
                    Log.e("tag","${response.code()}")
                }
            }
            override fun onFailure(call: Call<GetAllClassroomsResponse>, t: Throwable) {
                _listClassrooms.postValue(null)
                Log.e("tag","${t.message}")
            }
        })
    }

}