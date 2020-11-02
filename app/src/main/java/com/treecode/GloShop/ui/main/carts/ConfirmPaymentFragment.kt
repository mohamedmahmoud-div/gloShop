package com.treecode.GloShop.ui.main.carts

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.checkout.*
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.util.CartsManger
import com.treecode.GloShop.util.SessionManager
import com.example.mvvmcoorutines.data.api.ApiService
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_confirm_payment.*
import kotlinx.android.synthetic.main.fragment_confirm_payment.loading
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"
private const val ARG_PARAM4 = "param4"
private const val ARG_PARAM_IS_Cash = "is_cash"
private const val ARG_PARAM_Selected_GateWay = "gateway"

/**
 * A simple [Fragment] subclass.
 * Use the [ConfirmPaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConfirmPaymentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var totalCost: String? = null
    private var shippingCost: String? = null
    private var arrivalDate: String? = null
    private var addressBookID: Int? = null
    private var isCash = true
    private var selectedGateway:Int? = null
    private lateinit var apiInterface: ApiService
    private lateinit var webView: WebView
    private var isRedirect = false
    private var orderID :String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            totalCost = it.getString(ARG_PARAM1)
            shippingCost = it.getString(ARG_PARAM2)
            arrivalDate = it.getString(ARG_PARAM3)
            addressBookID = it.getInt(ARG_PARAM4)
            isCash = it.getBoolean(ARG_PARAM_IS_Cash)
            selectedGateway = it.getInt(ARG_PARAM_Selected_GateWay)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
            totalCost = it.getString(ARG_PARAM1)
            shippingCost = it.getString(ARG_PARAM2)
            arrivalDate = it.getString(ARG_PARAM3)
            addressBookID = it.getInt(ARG_PARAM4)
            text_shipping_fees.text = shippingCost
            text_total.text = totalCost
            if (arrivalDate != null){
                text_arrive_date.text = arrivalDate

            }else {
                text_arrive_date.visibility = View.GONE
                arrive_header.visibility = View.GONE
            }
            btn_confirm_payment.setOnClickListener {
                testProductsAvailabilityInStore()
            }
            btn_apply_promo_code.setOnClickListener {
                val promoCode = edit_promo_code.text.toString()
                if (promoCode.isNullOrEmpty()){
                    Toasty.warning(requireContext(),getString(R.string.insert_promo_code)).show()
                    return@setOnClickListener
                }
                applyPromoCode(promoCode)
            }
            webView = view.findViewById(R.id.webview)
            webView.clearCache(true)
            webView.settings.javaScriptEnabled = true
            webView.webChromeClient =WebChromeClient()

            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    loading.visibility = View.VISIBLE
                    return  false
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    loading.visibility= View.GONE
                }
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                    if (url == RetrofitBuilder.BASE_URL + "/"){
//                        view?.destroy()
//                        btn_close_web_view.visibility = View.GONE
//                        showPaymentTransactionStatus()
//                        loading.visibility= View.GONE
//                    }
                    super.onPageStarted(view, url, favicon)
                }
            }

            btn_close_web_view.setOnClickListener {
                dismissWebView()
            } }

    }
private fun showPaymentTransactionStatus(){
    val sessionManger = SessionManager(requireContext())
    val token = sessionManger.fetchAuthToken()
    if (token != null){
        val tokenToSend = "Token $token"
        if (orderID == null) {
            navigateToOrderFailFragment()
        return
        }
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        val checkPaymentStatusCall = apiInterface.checkOnlinePaymentStatus(orderID!!,tokenToSend)
        checkPaymentStatusCall.enqueue(object:Callback<OnlinePaymentResponse> {
            override fun onFailure(call: Call<OnlinePaymentResponse>, t: Throwable) {
                Toasty.error(requireContext(),getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG).show()
                call.cancel()
            }

            override fun onResponse(
                call: Call<OnlinePaymentResponse>,
                response: Response<OnlinePaymentResponse>
            ) {
                val onlinePaymentResponse = response.body()
                if (onlinePaymentResponse != null){
                    val onlinePaymentSuccess = onlinePaymentResponse.data
                    if (onlinePaymentSuccess.isSuccess) {
                        navigateToSuccessFragment()
                    }else{
                    navigateToOrderFailFragment()
                    }
                }else{
                  navigateToOrderFailFragment()
                }
            }

        })

    }
}
    private fun navigateToOrderFailFragment(){
        val orderFailFragment = OrderFailFragment()
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(orderFailFragment)
    }
    private fun dismissWebView(){
        webView.destroy()
        loading.visibility= View.GONE

        btn_close_web_view.visibility = View.GONE
       showPaymentTransactionStatus()
    }
    private fun confirmRequest(){

        val sessionManger = SessionManager(requireContext())
        val token = sessionManger.fetchAuthToken()
        if (token != null){
        val tokenToSend = "Token $token"
            val confirmPaymentRequest = ConfirmPaymentRequest(addressBookID = addressBookID!!,cashOnDelievry = isCash,selectedGateWayID = selectedGateway)
            val confirmCall = apiInterface.confirmPayment(confirmPaymentRequest,tokenToSend)
            confirmCall.enqueue(object : Callback<ConfirmPaymentResponse>{
                override fun onFailure(call: Call<ConfirmPaymentResponse>, t: Throwable) {
                    call.cancel()
                }

                override fun onResponse(
                    call: Call<ConfirmPaymentResponse>,
                    response: Response<ConfirmPaymentResponse>
                ) {
                    val erroBody = response.errorBody().toString()
                    val checkoutResponse: ConfirmPaymentResponse? = response.body()
                    val checkoutData = checkoutResponse?.data
                   if(checkoutData== null){
                        Toasty.error(requireContext(),getString(R.string.address_location_wron)).show()

                   }else {
                       if (!checkoutData!!.isSuccess){
                           Toasty.error(requireContext(),getString(R.string.already_ordered)).show()

                       }else{
                           orderID = checkoutData.orderID
                           val onlinePaymenUrl =  checkoutData.paymentUrl
                           if (onlinePaymenUrl ==null){
                               Toasty.success(requireContext(),getString(R.string.order_proceed)).show()
                               navigateToSuccessFragment()
                           }else {
                               // todo redirect to webview
                               navigateToBankGateway(onlinePaymenUrl)

                           }
                       }
                   }
                }

            })
        }else{
            Toasty.error(requireContext(),"Please Login First").show()
        }
    }
    private fun navigateToSuccessFragment(){
        val orderSuccesFragment = OrderSuccesFragment()
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(orderSuccesFragment)
    }
    private fun testProductsAvailabilityInStore(){
        val sessionManager = SessionManager(requireContext())
        val userToken = sessionManager.fetchAuthToken()
        val adapter = AvailableProductsAdapter()
        val cartsManger = CartsManger(requireContext())
        val productsInCart = cartsManger.getAllProducts()
        if (userToken.isNullOrEmpty()){
            Toasty.error(requireContext(),"Please Login And First",Toasty.LENGTH_LONG).show()
        }else {
            val token = "Token " + userToken!!
            var availableProductsQuantity = ArrayList<AvailableProductQuantity>()
            productsInCart?.forEach {
                val productID = it.id
                val productQuantity = adapter.convertCartProduct(it)
                availableProductsQuantity.add(productQuantity)
            }
            val testAvailbleProductsCall = apiInterface.testProductAvailability(
                AvailableProductsQuantityRequest(availableProductsQuantity),token)
            loading.visibility = View.VISIBLE
            testAvailbleProductsCall.enqueue(object : Callback<AvailableProductsResponse> {
                override fun onFailure(call: Call<AvailableProductsResponse>, t: Throwable) {

                    Toasty.error(
                        requireContext(),
                        getString(R.string.please_check_internet_connection),
                        Toasty.LENGTH_LONG
                    ).show()
                    loading.visibility  = View.GONE

                    call.cancel()
                }

                override fun onResponse(
                    call: Call<AvailableProductsResponse>,
                    response: Response<AvailableProductsResponse>
                ) {
                    if (response.code() == 200) {
                        val availableResponse = response.body()
                        val availProductData = availableResponse!!.data
                        val prdocuts = availProductData.products
                        if (prdocuts.isNullOrEmpty()) {
                            // all products out of Stock
                        } else {

                            compareProductsWithStoreAvailability(prdocuts)
                        }
                    }

                }

            })

        }
    }
    private fun applyPromoCode(promoCode:String){
        val sessionManger = SessionManager(requireContext())
        val token = sessionManger.fetchAuthToken()
        if (token != null){
            val tokenToSend = "Token $token"
            loading.visibility = View.VISIBLE
        val promoCodeCall = apiInterface.sendPromoCode(promoCode,addressBookID!!,tokenToSend)
            promoCodeCall.enqueue(object :Callback<PromoCodeResponse>{
                override fun onFailure(call: Call<PromoCodeResponse>, t: Throwable) {
                    loading.visibility = View.GONE

                    Toasty.error(requireContext(),getString(R.string.please_check_internet_connection)).show()
                    call.cancel()
                }

                override fun onResponse(
                    call: Call<PromoCodeResponse>,
                    response: Response<PromoCodeResponse>
                ) {
                    loading.visibility = View.GONE

                    val promoCodeResponse = response.body()
                    if (promoCodeResponse != null){
                        val promoCode = promoCodeResponse.data
                        val errorMessage = promoCodeResponse.message
                        if (promoCode!!.totalOrder == null){
                            Toasty.error(requireContext(),errorMessage!!).show()
                                }else {
                            text_discount_value.visibility = View.VISIBLE
                            discount_header.visibility = View.VISIBLE
                            text_discount_value.text= promoCode.discount.toString()
                            text_total.text = promoCode.totalCost.toString()
                        }
                    }
                }

            })
    }
    }
    private fun compareProductsWithStoreAvailability(storeProducts:ArrayList<ProductAvailability>){
        val cartsManger = CartsManger(requireContext())
        val updatedProducts = cartsManger.getAllProducts()
        var productsAvailabilityStatus = true
        storeProducts.forEach {storeProduct ->
            val productInCarts =  updatedProducts!!.filter { it.id ==storeProduct.productID }
            val productInCart = productInCarts.first()
            productInCart.totalAvailbilty =storeProduct.availability
            if (productInCart.totalAvailbilty!! < productInCart.quantity)
                productsAvailabilityStatus = false


        }
        if (productsAvailabilityStatus){
            confirmRequest()
        }else {
            backToCarts()
        }
    }
    private fun backToCarts(){
        val fragment = CartsFragment.newInstance()
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.popFragment(fragment)
        fc?.replaceFragment(fragment)
    }
    private fun navigateToBankGateway(paymentUrl:String){
        card_confirm_payment.visibility = View.GONE
        btn_confirm_payment.visibility = View.GONE
        btn_close_web_view.visibility = View.VISIBLE
        webView.loadUrl(paymentUrl)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_payment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param totalCost Parameter 1.
         * @param shippinCost Parameter 2.
         * @return A new instance of fragment ConfirmPaymentFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(totalCost: String, shippinCost: String,shippingDate:String?= "",addressBookID:Int,isCash:Boolean,selectedGateway:Int?) =
            ConfirmPaymentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, totalCost)
                    putString(ARG_PARAM2, shippinCost)
                    putString(ARG_PARAM3, shippingDate)
                    putInt(ARG_PARAM4, addressBookID)
                    putBoolean(ARG_PARAM_IS_Cash,isCash)
                    if (selectedGateway == null)
                    putInt(ARG_PARAM_Selected_GateWay,0)
                    else
                    putInt(ARG_PARAM_Selected_GateWay,selectedGateway)

                }
            }
    }
}