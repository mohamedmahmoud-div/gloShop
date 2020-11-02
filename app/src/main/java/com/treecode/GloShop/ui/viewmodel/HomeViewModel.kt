package com.treecode.GloShop.ui.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.treecode.GloShop.data.repository.MainRepository
import com.example.mvvmcoorutines.util.Resource
import kotlinx.coroutines.Dispatchers

class HomeViewModel(private val mainRepository: MainRepository) : ViewModel() {

    fun getHome() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getHome()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, msg = exception.message ?: "Error Occurred!"))
        }
    }
}