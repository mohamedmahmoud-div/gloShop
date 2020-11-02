package com.treecode.GloShop.data.model.home

import com.google.gson.annotations.SerializedName

class Department (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("products") val products : ArrayList<Product>
)