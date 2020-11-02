package com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError:Int? = null,
    val emailError:Int? = null,
    val fullNameError:Int? = null,
    val dateError:Int? = null,
    val isDataValid: Boolean = false

)