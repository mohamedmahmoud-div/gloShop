package com.example.mvvmcoorutines.util

data class  Resource<out T>(val status: Status,val data:T?,val message:String?){
    companion object {
        fun <T> success(data:T):Resource<T> = Resource(status = Status.SUCCESS,data = data,message = null)
        fun<T> loading(data:T?):Resource<T> = Resource(status = Status.LOADING,data = data,message = null)
        fun<T> error(data:T?,msg:String):Resource<T> = Resource(Status.ERROR,data,message = msg)
    }
}