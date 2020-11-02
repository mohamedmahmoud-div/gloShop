package com.example.mvvmcoorutines.data.api

import com.treecode.GloShop.data.model.UserLoginRequest
import com.treecode.GloShop.data.model.login.LoginResponse
import com.treecode.GloShop.data.model.search.SearchQueryRequest
import com.treecode.GloShop.data.model.search.SearchResponse
import retrofit2.Call

class ApiHelper(private val apiService: ApiService) {

    suspend fun getHome() =  apiService.getHomeData()
    suspend fun getSearchData (request:SearchQueryRequest): Call<SearchResponse> {
      return  apiService.testSearchProducts(null,null,null,1)
    }
    suspend fun login(userLoginRequest: UserLoginRequest): LoginResponse {
val response = apiService.login(userLoginRequest.username,userLoginRequest.password)
    return response
    }

}