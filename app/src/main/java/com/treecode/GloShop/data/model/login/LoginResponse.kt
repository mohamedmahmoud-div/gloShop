package com.treecode.GloShop.data.model.login

import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class LoginResponse {
    @SerializedName("status")
    var status: Int? = 0

    @SerializedName("status_code")
    var statusCode:Int? = 0

    @SerializedName("message")
    var message:String? = ""

//    @SerializedName("errors")
//    var errors:ArrayList<String>? = ArrayList<String>()

    @SerializedName("data")
    var data:AccountUser? = null
    @SerializedName("errors")
    var error:ArrayList<String>? = null


    }
class AccountUser {
    @SerializedName("token")
    var token:String? = ""

    @SerializedName("id")
    var id:Int? = 0

    @SerializedName("username")
    var username:String? = ""

    @SerializedName("email")
    var email:String? = ""

    @SerializedName("full_name")
    var fullName:String? = ""

//        @SerializedName("gender")
//        var gender:Int? = null // Male 1 , Female 2

    @SerializedName("birth_date")
    var birthDate:String? = ""


}
