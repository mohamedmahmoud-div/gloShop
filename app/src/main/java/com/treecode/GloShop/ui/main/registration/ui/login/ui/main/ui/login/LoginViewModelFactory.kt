package com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.data.LoginDataSource
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.data.LoginRepository
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.register.SignupViewModel

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()
                )
            ) as T
        } else if(modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            return  SignupViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}