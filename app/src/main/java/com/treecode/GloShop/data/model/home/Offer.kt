package com.treecode.GloShop.data.model.home

import com.google.gson.annotations.SerializedName

class Offer (
    @SerializedName("id") val id : Int,
    @SerializedName("product") val product: Product ,
    @SerializedName("image") val offerImage: String ,
    @SerializedName("discount") val discount: Double ,
    @SerializedName("type") val type: OfferType,
    @SerializedName("expire_date") val expireDate: String
    )
class OfferType(
    @SerializedName("name") val name: String

    )