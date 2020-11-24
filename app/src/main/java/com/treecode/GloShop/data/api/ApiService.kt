package com.example.mvvmcoorutines.data.api

import com.treecode.GloShop.data.model.home.HomeResponse
import com.treecode.GloShop.data.model.UserLoginRequest
import com.treecode.GloShop.data.model.UserSignupRequest
import com.treecode.GloShop.data.model.checkout.*
import com.treecode.GloShop.data.model.login.*
import com.treecode.GloShop.data.model.profile.*
import com.treecode.GloShop.data.model.search.*
import com.treecode.GloShop.util.Constants
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/api/v1/home/")
    suspend fun getHomeData(): HomeResponse
@GET("api/v1//products/")
suspend fun getSearchProducts(@Query("category")category:Int?): SearchResponse

    @GET("/api/v1/products/")
    fun testSearchProducts(@Query("category") categoryID: Int?, @Query("collection")collectioID:Int?,@Query("search")searchText:String?,@Query("page") pageNumber:Int?,@Query("page_size") pageSize:Int = 5):Call<SearchResponse>
    @GET("/api/v1/products/{id}/")
    fun getProductDetails(@Path("id")id:Int):Call<ProductDetailsResponse>

    @FormUrlEncoded
    @POST("/api/v1/login/")
    suspend fun login(@Field("username_email")username:String,@Field("password") password:String):LoginResponse

@POST("/api/v1/send-filter/")
fun applySearchFilter(@Body filterWith: ApplyFilterRequest):Call<FilterSearchRepsonse>

    @FormUrlEncoded
    @POST("/api/v1/login/")
      fun loginTest(@Field("username_email")username:String,@Field("password") password:String):Call<LoginResponse>

@POST("/api/v1/register/")
fun signup(@Body signupRequest: SignupRequest):Call<LoginResponse>

  @POST("/api/v1/social-register/")
  fun socialSignUp(@Body socialSignUpRequest: SocialSignUpRequest):Call<LoginResponse>

    @POST fun signup(@Body user:UserSignupRequest)
@POST("/api/v1/login/")
fun loginUser(@Body user:UserLoginRequest)

@GET("/api/v1/countries/")
fun getCountries(@Header("Authorization") token: String):Call<CountryAddressResonse>

@POST("/api/v1/address-book/")
    fun addAddress(@Body locationRequest:CreateLocationRequest,@Header("Authorization") token: String):Call<LocationRepsonse>
    @GET("/api/v1/orders/")
    fun getDeliverdOrders(@Header("Authorization") token: String,@Query("status")statusID:Int = Constants.ORDERED):Call<OrderResponse>

    @GET("/api/v1/orders/")
    fun getPendingShipments(@Header("Authorization") token: String):Call<OrderResponse>

    @FormUrlEncoded
    @POST("/api/v1/send-product-spec-value-id/")
    fun addNextSpecOfProductWith(@Field("spec_value_id")specValueID:Int):Call<NextProductSpecResponse>

    @POST("/api/v1/shopping-cart/")
    fun porceedToCheckout(@Body checkoutRequest:CheckoutRequest,@Header("Authorization") token: String):Call<CheckoutResponse>

    @FormUrlEncoded
    @POST("/api/v1/socila-login/")
    fun socialLogin(@Field("facebook_google_id")socialID:String,@Field("social_type")socialType:String):Call<LoginResponse>


    @GET("/api/v1/address-book/")
    fun getAddressBook(@Header("Authorization") token: String):Call<AddressBookResponse>


    @PATCH("/api/v1/address-book/{id}/")
    fun updateAddressBook(@Path("id")id:Int,@Body locationRequest:CreateLocationRequest,@Header("Authorization") token: String):Call<LocationRepsonse>

    @DELETE("/api/v1/address-book/{id}/")
    fun deleteAddressBook(@Path("id")id:Int,@Header("Authorization") token: String):Call<LocationRepsonse>

    @POST("/api/v1/check-shopping-cart/")
    fun testProductAvailability(@Body availableProductsQuantityRequest: AvailableProductsQuantityRequest,@Header("Authorization") token: String):Call<AvailableProductsResponse>

    @POST("/api/v1/check-out/")
    fun confirmPayment(@Body confirmPaymentRequest:ConfirmPaymentRequest,@Header("Authorization") token: String):Call<ConfirmPaymentResponse>

    @GET("/api/v1/more/")
    fun getPolicesORAbout(@Query("model")type:String):Call<PoiicesResonse>

    @POST("/api/v1/password_reset/confirm/")
    fun passwordReset(@Body changePasswordRequest: ChangePasswordRequest):Call<ChangePasswordResponse>

    @FormUrlEncoded
    @POST("/api/v1/password_reset/")
    fun sendToMyEmail(@Field("email")email:String):Call<DataBody>

    @GET("/api/v1/contact-us/")
    fun getContactUS():Call<ContactUSResponse>
    @FormUrlEncoded
    @POST("/api/v1/contact-us/")
    fun sendMessageToSeller(@Field("name")fullName:String,@Field("email")email: String,@Field("message")message:String):Call<ContactUserMessageRespons>

    @GET("/api/v1/payments-api/")
    fun getAvailableGateWays(@Header("Authorization") token: String):Call<AvailaiblePaymentGatewayResponse>
    @FormUrlEncoded
    @POST("/api/v1/check-payment-status/")
    fun checkOnlinePaymentStatus(@Field("order_id") orderID:String,@Header("Authorization") token: String):Call<OnlinePaymentResponse>
    @FormUrlEncoded
    @POST("/api/v1/send-promo-code/")
    fun sendPromoCode(@Field("code")code:String,@Field("address_book_id")addressID:Int,@Header("Authorization") token: String):Call<PromoCodeResponse>
    @GET("/api/v1/users/{id}")
    fun getProfileDate(@Query("id")userID:String,@Header("Authorization") token: String):Call<LoginResponse>
    @FormUrlEncoded
    @PATCH("/api/v1/users/{id}/")
    fun updateProfile(@Field("store_name")storeName:String, @Path("id")userID:Int, @Header("Authorization") token: String):Call<LoginResponse>

}
//,@Query("page") pageNumber:Int?,@Query("page_size") pageSize:Int = 15