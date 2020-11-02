package com.treecode.GloShop.data.model.profile

import com.google.gson.annotations.SerializedName

class CountryAddressResonse (
    @SerializedName("status")
    var status: Int = 0,

    @SerializedName("status_code")
    var statusCode:Int = 0,

    @SerializedName("message")
    var message:String = "",
    @SerializedName("data")
    var countries:ArrayList<Country>?
)
class Country (
    @SerializedName("id")
    val id :Int ,
    @SerializedName("name")
    val name:String,
    @SerializedName("cities")
    var  cities :ArrayList<City>

)
class City (
    @SerializedName("id")
    val id :Int ,
    @SerializedName("name")
    val name:String

)
class CreateLocationRequest(
    @SerializedName("id")
    val id: Int,
    @SerializedName("country")
    val countryID :Int,

    @SerializedName("city")
    val cityId:Int,

    @SerializedName("street_address")
    val streetAddress:String,

    @SerializedName("zip_code")
    val postCode:Int,

    @SerializedName("region")
    val region:String,

    @SerializedName("phone")
    val phoneNumber:String
)
class LocationRepsonse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",
    @SerializedName("errors")
    var error:ArrayList<String>?,

    @SerializedName("data")
    val data:CreateLocationRequest
)
class ErrorResponse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",
    @SerializedName("errors")
    var error:ArrayList<String>
)
class AddressBookResponse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",
    @SerializedName("errors")
    var error:ArrayList<String>,
    @SerializedName("data")
    val data:HashSet<AddressBook>
)
class  AddressBook(

    @SerializedName("id")
    val id :Int ,
    @SerializedName("country")
    var country :CountryAddressBook,
    @SerializedName("city")
        var city :City,
    @SerializedName("street_address")
    var streetAddress: String,
    @SerializedName("region")
    var region: String,
    @SerializedName("phone")
    var phone: String
)
class CountryAddressBook(
    @SerializedName("id")
    val id :Int ,
    @SerializedName("name")
    val name:String


)
/*  "street_address": "222",
            "zip_code": "2",
            "region": "moktam",
            "phone": "01145805981"*/