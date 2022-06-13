package com.example.invscan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.invscan.data.repository.RepositoryImpl
import com.example.invscan.domain.enteties.GetItemResponse
import com.example.invscan.domain.enteties.InventoryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScanViewModel:ViewModel() {

    private val repository = RepositoryImpl()

    private val _foundedItem = MutableLiveData<InventoryItem?>()
    val foundedItem:LiveData<InventoryItem?>
    get() = _foundedItem

    fun getItemByNum(num:String){
        if (num.isNotEmpty()){
            repository.getItemByNum(num).enqueue(object : Callback<GetItemResponse>{
                override fun onResponse(
                    call: Call<GetItemResponse>,
                    response: Response<GetItemResponse>
                ) {
                    if (response.isSuccessful){
                        response.body()?.let {
                            _foundedItem.postValue(it.item)
                        }
                    } else {
                        _foundedItem.postValue(null)
                        Log.e("tag","${response.code()}")
                    }
                }
                override fun onFailure(call: Call<GetItemResponse>, t: Throwable) {
                    _foundedItem.postValue(null)
                    Log.e("tag","${t.message}")
                }
            })
        }
    }

}