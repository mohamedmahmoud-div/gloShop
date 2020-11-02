package com.treecode.GloShop.data.model

import com.google.gson.annotations.SerializedName

class HotDeal (
    @SerializedName("id")
    var dealID: Int = 0,

    @SerializedName("name")
    var dealName: String = "",

    @SerializedName("offer") val offer:OfferHotDeal
)
class OfferHotDeal(
    @SerializedName("type")
val type: HotDealType,
    @SerializedName("image")
    val image:String,
    @SerializedName("discount")
    val discount:Int,
    @SerializedName("description")
    val description: String
)
class HotDealType(
    @SerializedName("name")
    val name:String
)
/*
*   "offer": {
                    "type": {
                        "name": "Top sale"
                    },
                    "image": "http://161.35.113.112:8024/media/offer_images/man-1.jpg",
                    "discount": 30.0
                }
* */