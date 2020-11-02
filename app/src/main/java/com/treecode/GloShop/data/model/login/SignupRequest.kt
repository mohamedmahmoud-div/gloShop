package com.treecode.GloShop.data.model.login

import com.google.gson.annotations.SerializedName

class SignupRequest (

    @SerializedName("username")
    val username: String ,
    @SerializedName("email")
    val email: String ,
@SerializedName("gender")
val gender:Int = 1,
    @SerializedName("birth_date")
    val birthDate: String ,

    @SerializedName("new_password1")
    val password:String,
    @SerializedName("new_password2")
    val confirmPassword:String,
    @SerializedName("full_name")
    val fullName:String
)
class SocialSignUpRequest(
    @SerializedName("username")
    val username: String ,
    @SerializedName("email")
    val email: String ,
    @SerializedName("gender")
    val gender:Int = 1,
    @SerializedName("birth_date")
    val birthDate: String ,
    @SerializedName("full_name")
    val fullName:String,
    @SerializedName("facebook_id")
    val faceBookID:String?,
    @SerializedName("google_id")
    val googleID:String?
    )

class SocialCallBackData(
    val email:String,
    val fullName: String,
    val id:String
)