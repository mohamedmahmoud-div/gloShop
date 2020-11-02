package com.treecode.GloShop.data.model.search

import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.data.model.home.ProductCategory
import com.google.gson.annotations.SerializedName

class ProductDetailsResponse(
    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data : ProductDetailsData
)
class ProductDetailsData(
    @SerializedName("id") var id : Int,
    @SerializedName("name") var name : String,
    @SerializedName("main_img") var img : String,
    @SerializedName("multi_images") var otherImages : ArrayList<ProductMultipleImage>,
    @SerializedName("price") var price : Int,
    @SerializedName("price_after") var priceAfterDicount : Int?,
    @SerializedName("discount") var discount : Int?,
    @SerializedName("offer_type") var offerType : String?,
    @SerializedName("stars") var stars : Double,
    @SerializedName("reviews") var reviews : Int,
    @SerializedName("availability") var availability : Int,
    @SerializedName("specs") var specification: Specification?,
    @SerializedName("short_description") var description: String,
    @SerializedName("category") var category: ProductCategory,
    @SerializedName("weight") var weight:Double,
    @SerializedName("volume") var volume:Double,
    @SerializedName("product_reviews") var productReviews:ArrayList<ProductReview>?,


    @SerializedName("related_products") var relatedProducts:ArrayList<Product>


    )
class ProductMultipleImage(
    @SerializedName("image") var image : String
    )
class ProductReview(
    @SerializedName("id") var id : Int,
    @SerializedName("buyer") var buyer: ReviewBuyer ,
    @SerializedName("stars") var stars : Double,
    @SerializedName("comment") var comment : String
    )
class ReviewBuyer(
    @SerializedName("id") var id : Int,
    @SerializedName("full_name") var name : String

    )

class  NextProductSpecResponse(
    @SerializedName("status") var status : Int,
    @SerializedName("status_code") var  status_code : Int,
    @SerializedName("message") var message : String,
    @SerializedName("data") var data : ArrayList<Specification>

)