package com.treecode.GloShop.data.model.login

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class ChangePasswordRequest(
    @SerializedName("uid")
    val uid:String,
    @SerializedName("token")
    val token:String,
    @SerializedName("new_password1")
    val newPassword:String,
    @SerializedName("new_password2")
    val confirmPassword:String
)
class ChangePasswordResponse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
var statusCode:Int? = 0,

@SerializedName("message")
var message:String? = "",
@SerializedName("errors")
var error: ArrayList<String>? = null

)
class  DataBody(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",
    @SerializedName("errors")
    var error: ArrayList<String>? = null
)
