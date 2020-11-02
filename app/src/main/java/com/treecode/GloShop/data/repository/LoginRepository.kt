package com.treecode.GloShop.data.repository

import com.treecode.GloShop.data.model.UserLoginRequest
import com.treecode.GloShop.data.model.login.LoginResponse
import com.treecode.GloShop.ui.main.registration.data.model.LoggedInUser
import com.example.mvvmcoorutines.data.api.ApiHelper

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(private val apiHelper: ApiHelper) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        //ataSource.logout()
    }
    suspend fun login(username: String, password: String) : LoginResponse {
    return apiHelper.login(UserLoginRequest(username,password))

    }
//    suspend fun login(username: String, password: String): Result<LoggedInUser> {
//        // handle login
//        //Todo Inject Api Helper and call apiHelper login method
//        val loginRequest = UserLoginRequest(username,password)
//
////        val result = dataSource.login(username, password)
////
////        if (result is Result.Success) {
////            setLoggedInUser(result.data)
////        }
//
//        return result
//    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }
}