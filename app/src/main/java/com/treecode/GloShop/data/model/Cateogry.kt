package com.treecode.GloShop.data.model

import com.google.gson.annotations.SerializedName

class Cateogry(
    @SerializedName("id") val id : Int,

    @SerializedName("name")
    var name:String = "",
    @SerializedName("image_banner")
    var image:String = ""

)