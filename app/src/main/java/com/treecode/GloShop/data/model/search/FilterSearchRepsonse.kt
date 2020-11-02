package com.treecode.GloShop.data.model.search

import com.treecode.GloShop.data.model.home.Product
import com.google.gson.annotations.SerializedName

class FilterSearchRepsonse (
    @SerializedName("status") var status : Int,
@SerializedName("status_code") var  status_code : Int,
@SerializedName("message") var message : String,
@SerializedName("data") var data : ArrayList<Product>
)
class FilterSearchDataResponse (
    @SerializedName("products") var products:ArrayList<Product>,
    @SerializedName("specs") var specifications:ArrayList<Specification>

)
class ApplyFilterRequest(
    @SerializedName("ids") var ids : ArrayList<Int>,
    @SerializedName("search") var searchText :String = "",
    @SerializedName("category_id") var categoryID:Int?,
    @SerializedName("collection_id") var collectionID:Int?

)