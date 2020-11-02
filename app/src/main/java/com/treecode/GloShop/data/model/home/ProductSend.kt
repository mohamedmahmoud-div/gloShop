package com.treecode.GloShop.data.model.home

import com.google.gson.annotations.SerializedName

class ProductSend (
    @SerializedName("id") val id : Int,

    val specId:ArrayList<Int>,

    val specValueID:ArrayList<Int>
)

class SpecCart{

}