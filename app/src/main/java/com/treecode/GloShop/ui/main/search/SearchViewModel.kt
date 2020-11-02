package com.treecode.GloShop.ui.main.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.treecode.GloShop.data.model.search.SearchQueryRequest
import com.treecode.GloShop.data.repository.MainRepository
import com.example.mvvmcoorutines.util.Resource
import kotlinx.coroutines.Dispatchers

class SearchViewModel (private val mainRepository: MainRepository) : ViewModel() {

    fun getSearch(request: SearchQueryRequest) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getSearch(request)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, msg = exception.message ?: "Error Occurred!"))
        }
    }
    fun getSearchData(request: SearchQueryRequest){

    }
}