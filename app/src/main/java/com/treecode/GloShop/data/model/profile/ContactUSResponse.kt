package com.treecode.GloShop.data.model.profile

import com.google.gson.annotations.SerializedName

class ContactUSResponse (
    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("data") val  data: ArrayList<ContactUsData?>
)
class ContactUsData (
@SerializedName("phone")
val phone:String,
@SerializedName("email")
val email:String,
@SerializedName("address")
val address:String,
@SerializedName("postal_code")
val postalCode:String?,
@SerializedName("facebook")
val faceBookLink:String?,
@SerializedName("twitter")
val twitterLink:String?,
@SerializedName("google")
val googleLink:String?,
@SerializedName("youtube")
val youtubeLink:String?,
@SerializedName("instagram")
val insgragmLink:String?
)

class ContactUserMessageRespons(
    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("errors")
    val errors:ArrayList<String>?,
    @SerializedName("data") val  data: ContactUSMessageData?
)
class  ContactUSMessageData (
@SerializedName("name")
val name:String,
@SerializedName("email")
val email:String,
@SerializedName("message")
val message:String
)
/* "phone": "201141546724",
            "email": "info@fastfood.com",
            "address": "Almukattam-Cairo-Egypt",
            "postal_code": "12345",
            "facebook": "https://www.facebook.com",
            "twitter": "https://www.twitter.com",
            "google": "https://www.google.com",
            "youtube": "https://www.youtube.com",
            "instagram": "https://www.instagram.com"
* */