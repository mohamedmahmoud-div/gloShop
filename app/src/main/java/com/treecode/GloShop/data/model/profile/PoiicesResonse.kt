package com.treecode.GloShop.data.model.profile

import com.google.gson.annotations.SerializedName

class PoiicesResonse (
    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("data") val  data: PolicesData
)
class PolicesData(
    @SerializedName("details")
    val details:String?
)