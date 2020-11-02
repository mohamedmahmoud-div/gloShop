package com.treecode.GloShop.data.model

import com.google.gson.annotations.SerializedName

class UserLoginRequest (
@SerializedName("username_email")
    val username: String ,
@SerializedName("password")
    val password:String
)
