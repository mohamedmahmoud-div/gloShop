package com.treecode.GloShop.ui.main.registration.ui.login.ui.main.register

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login.LoginFormState
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

class SignupViewModel ():ViewModel(){
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm


    fun loginDataChanged(username: String, password: String,confirmPassword: String,fullName:String,email:String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        }else if (!isConfirmPasswordValid(password,confirmPassword)) {
            _loginForm.value =
                LoginFormState(confirmPasswordError = R.string.invalid_confirm_password)

        }else if (!isEmailValid(email)) {

            _loginForm.value =
                LoginFormState(emailError = R.string.invalid_email)


        }else if (!isUserNameValid(fullName)) {
            _loginForm.value =
                LoginFormState(fullNameError = R.string.invalid_fullname)
        }else   {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }
private fun isEmailValid(email:String): Boolean {
    return if (email.contains("@")) {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    } else {
        email.isNotBlank()
    }
}
private fun  isDateValid(date:String):Boolean{
    val sdf: DateFormat = SimpleDateFormat("MM/dd/yyyy")
    sdf.setLenient(false)

    try {
        sdf.parse(date)
        return true
    } catch (e: ParseException) {
        return false
    }
}

    private fun isUserNameValid(username: String): Boolean {
        return username.isNotBlank()

    }
    private fun isConfirmPasswordValid(password:String,confirmPassword:String):Boolean{
        return (password == confirmPassword && password.length > 7)
    }
    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }
}