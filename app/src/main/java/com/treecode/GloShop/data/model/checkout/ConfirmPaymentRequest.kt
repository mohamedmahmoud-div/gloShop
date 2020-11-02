package com.treecode.GloShop.data.model.checkout

import com.google.gson.annotations.SerializedName

class ConfirmPaymentRequest (
    @SerializedName("address_book_id") val addressBookID:Int,
    @SerializedName("is_cash") var cashOnDelievry:Boolean,
    @SerializedName("payment_api_id") var selectedGateWayID:Int?
)

class ConfirmPaymentResponse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",

    @SerializedName("errors")
    var error:ArrayList<String> ?= null,
@SerializedName("data")
val data:ConfirmPaymentData
)
class ConfirmPaymentData(
    @SerializedName("payment_url")
    val paymentUrl:String?,
    @SerializedName("order_id") val orderID:String,
    @SerializedName("done")
    val isSuccess:Boolean

)