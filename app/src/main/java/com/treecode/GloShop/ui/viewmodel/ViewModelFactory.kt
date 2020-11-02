package com.treecode.GloShop.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.treecode.GloShop.data.repository.LoginRepository
import com.treecode.GloShop.data.repository.MainRepository
import com.treecode.GloShop.ui.main.registration.ui.login.LoginViewModel
import com.treecode.GloShop.ui.main.search.SearchViewModel
import com.example.mvvmcoorutines.data.api.ApiHelper

class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(MainRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(LoginRepository(apiHelper)) as T
        } else if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(MainRepository(apiHelper)) as T

        }
        throw IllegalArgumentException("Unknown class name")

    }
}