package com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)