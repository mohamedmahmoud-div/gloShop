package com.treecode.GloShop.data.repository

import com.treecode.GloShop.data.model.search.SearchQueryRequest
import com.treecode.GloShop.data.model.search.SearchResponse
import com.example.mvvmcoorutines.data.api.ApiHelper
import retrofit2.Call

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getHome() = apiHelper.getHome()
    suspend fun getSearch(request:SearchQueryRequest): Call<SearchResponse> {
       return  apiHelper.getSearchData(request)
    }
}