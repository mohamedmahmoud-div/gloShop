package com.treecode.GloShop.data.model.checkout

import com.google.gson.annotations.SerializedName

class AvailaiblePaymentGatewayResponse (
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",

    @SerializedName("errors")
    var error:ArrayList<String> ?= null,
    @SerializedName("data")
    val  data:AvailablePaymentGatewayData

)
class AvailablePaymentGatewayData(
    @SerializedName("available_payments") val availablePaymentGateways:ArrayList<AvailabelPaymentGateway>,
    @SerializedName("done") val done:Boolean?
)
class AvailabelPaymentGateway(
    @SerializedName("id")
    val id :Int,
    @SerializedName("name")
    val name:String,
    @SerializedName("logo")
    val logo:String
)
class OnlinePaymentResponse(
    @SerializedName("status")
    var status: Int? = 0,

    @SerializedName("status_code")
    var statusCode:Int? = 0,

    @SerializedName("message")
    var message:String? = "",

    @SerializedName("errors")
    var error:ArrayList<String> ?= null,
    @SerializedName("data")
    val  data:OnlinePaymentSuccess
)
class  OnlinePaymentSuccess(
    @SerializedName("success")
    val isSuccess:Boolean
)