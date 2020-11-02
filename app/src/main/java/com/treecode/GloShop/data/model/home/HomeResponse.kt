package com.treecode.GloShop.data.model.home

import com.google.gson.annotations.SerializedName

 class HomeResponse (
    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data : Home? = null
)