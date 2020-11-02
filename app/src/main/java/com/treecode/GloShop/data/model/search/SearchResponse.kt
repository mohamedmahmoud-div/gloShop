package com.treecode.GloShop.data.model.search

import com.treecode.GloShop.data.model.home.Product
import com.google.gson.annotations.SerializedName

class SearchResponse  (
    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data : SearchDataResponse,
    @SerializedName("pagination") var pagination: Pagination?
)
class SearchDataResponse (
    @SerializedName("products") var products:ArrayList<Product>,
    @SerializedName("filter_specs") var specifications:ArrayList<Specification>?

)
class Specification (
@SerializedName("id") var id :Int,
@SerializedName("name") var name :String,
@SerializedName("is_color") var isColor:Boolean ,
@SerializedName("values") var values :ArrayList<SpecValue>,
var toatlavailbality:Int
)
class  SpecValue (
    @SerializedName("id") var id :Int,
    @SerializedName("value") var value :String,
    @SerializedName("quantity") var quantity:Int

)
class Pagination(
    @SerializedName("count") var totalProductsCount :Int,
    @SerializedName("current") var currentPage :Int,
    @SerializedName("pages") var totalNumberOfPages :Int,
    @SerializedName("next") var nextPage :String?,
@SerializedName("previous") var previousPage :String?
)
