package com.example.fincaredemo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fincaredemo.repository.Api
import com.example.fincaredemo.repository.ApiRepository
import org.json.JSONObject

class UserInfoViewModel : ViewModel() {

    private val apiRepository: ApiRepository = ApiRepository()
    private val api = Api.initRetrofit()

    fun getUserList(params: Map<String, String>): LiveData<JSONObject> {
        val call = api.getUserList(params)
        return apiRepository.callApi(call)
    }
}