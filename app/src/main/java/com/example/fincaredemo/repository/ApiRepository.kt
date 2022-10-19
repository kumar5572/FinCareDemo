package com.example.fincaredemo.repository

import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class ApiRepository {

    fun callApi(call: Call<ResponseBody>): MutableLiveData<JSONObject> {
        val mutableLiveData = MutableLiveData<JSONObject>()
        try {
            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: retrofit2.Response<ResponseBody>,
                ) {
                    if (response.isSuccessful) {
                        val obj = JSONObject(response.body()?.string()!!)
                        mutableLiveData.value = obj
                    } else {
                        val errorString = response.errorBody().toString()
                        val obj = JSONObject(errorString)
                        obj.put("code", response.code())
                        mutableLiveData.value = obj
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    val message: String = t.message.toString()
                    //Log.d("failure", message)

                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mutableLiveData
    }
}