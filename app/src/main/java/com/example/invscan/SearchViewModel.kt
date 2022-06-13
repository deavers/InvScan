package com.example.invscan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.invscan.data.repository.RepositoryImpl
import com.example.invscan.domain.enteties.GetClassroomResponse
import com.example.invscan.domain.enteties.InventoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel:ViewModel() {
    private val repository = RepositoryImpl()

    private val _items = MutableLiveData<List<InventoryItem>?>()
    val items:LiveData<List<InventoryItem>?>
    get() = _items

    fun getItemsByClassRoomNum(num:String){
        if (num.isNotEmpty()){
            repository.getItemsByClassroomNum(num).enqueue(object : Callback<GetClassroomResponse>{
                override fun onResponse(
                    call: Call<GetClassroomResponse>,
                    response: Response<GetClassroomResponse>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            _items.postValue(it.classroom.items)
                        }
                    } else {
                        _items.postValue(null)
                        Log.e("tag","${response.code()}")
                    }
                }
                override fun onFailure(call: Call<GetClassroomResponse>, t: Throwable) {
                    _items.postValue(null)
                    Log.e("tag","${t.message}")
                }
            })
        }
    }

}