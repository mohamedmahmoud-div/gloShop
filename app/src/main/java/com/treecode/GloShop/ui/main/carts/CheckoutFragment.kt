package com.treecode.GloShop.ui.main.carts

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.checkout.*
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.data.model.profile.CreditCard
import com.treecode.GloShop.ui.adapter.carts.AvailablePaymentGatewayAdapter
import com.treecode.GloShop.ui.adapter.carts.OrderCheckoutAdapter
import com.treecode.GloShop.ui.adapter.profile.SavedCreditAdapter
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.profile.addressbook.MyAdressBookFragment
import com.treecode.GloShop.util.*
import com.example.mvvmcoorutines.data.api.ApiService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bottom_sheet_checkout.*
import kotlinx.android.synthetic.main.fragment_checkout.*
import kotlinx.android.synthetic.main.fragment_checkout.layout_to_checkout
import retrofit2.Call

import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutFragment : Fragment() {
    private var testAvailbleProductsCall: Call<AvailableProductsResponse>? = null
    private  var productsInCart: HashSet<CartProduct>? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var cartAdapter : OrderCheckoutAdapter
    private lateinit var creditCardAdapter: SavedCreditAdapter
    private var products = HashSet<CartProduct>()
    private var creditCards = HashSet<CreditCard>()
    private lateinit var apiInterface: ApiService
private var addressID:Int? = null
    private  var  orderID:String? =null
    var isCash = true
    var selectedGatWay:Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textBar = requireActivity().findViewById(R.id.text_carts_bar_title) as TextView?
        textBar?.text = getString(R.string.checkout_title)
        setupUI()
        getDefaultAddress()
        text_order_payment_header
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        getCarts()
        btn_change_address.setOnClickListener{
            navigateToMyAddressBook()
        }

        radio_visa.setOnClickListener{
            if (selectedGatWay == null){
                retrieveAvailablePaymentGateways()
            }
           isCash = false
            recycler_payments_gateway.visibility = View.VISIBLE
        }
        radio_cash.setOnClickListener{
            isCash = true
            recycler_payments_gateway.visibility = View.GONE

        }
        bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet_products)
        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        card_total.visibility = View.VISIBLE

                    }
                    BottomSheetBehavior.STATE_EXPANDED ->{
                        card_total.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED ->{

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        card_total.visibility = View.GONE

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        card_total.visibility = View.GONE

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        card_total.visibility = View.GONE

                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
      /*  btn_see_more_products.setOnClickListener{
            expandCollapseSheet()

        }*/
        retrieveAvailablePaymentGateways()
    }

    private fun expandCollapseSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottom_sheet_products.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottom_sheet_products.visibility = View.GONE

        }
    }
    private fun retrieveAvailablePaymentGateways(){
        val sessionManager = SessionManager(requireContext())
        val token = sessionManager.fetchAuthToken()
        if (token.isNullOrEmpty()) {
            Toasty.error(requireContext(), getString(R.string.login_first), Toasty.LENGTH_LONG).show()
        }  else {
            val tokenToSend = "Token " + token!!
            val availablePaymentCall  = apiInterface.getAvailableGateWays(tokenToSend)
            loading.visibility = View.VISIBLE
            availablePaymentCall.enqueue(object :Callback<AvailaiblePaymentGatewayResponse>{
                @SuppressLint("StringFormatInvalid")
                override fun onFailure(call: Call<AvailaiblePaymentGatewayResponse>, t: Throwable) {
                    Toasty.error(requireContext(),getString(R.string.please_check_internet_connection,Toasty.LENGTH_LONG,true)).show()
                    call.cancel()
                    loading.visibility = View.GONE
                }
                override fun onResponse(
                    call: Call<AvailaiblePaymentGatewayResponse>,
                    response: Response<AvailaiblePaymentGatewayResponse>
                ) {
                    loading.visibility = View.GONE
                    if(response.code() == 200){
                        val avaliablePaymentsResponse = response.body()
                        if (avaliablePaymentsResponse != null){
                            if(avaliablePaymentsResponse.data.done == null){
                                setupAvailablePaymentsAdapter(avaliablePaymentsResponse.data.availablePaymentGateways)
                            } else {
                                val message = avaliablePaymentsResponse.message
                                if (message !=null)
                                    Toasty.error(requireContext(),message,Toasty.LENGTH_LONG,true).show()
                            }

                        }

                    }
                }

            })

        }

    }
    private fun setupAvailablePaymentsAdapter(gateways:ArrayList<AvailabelPaymentGateway>){
        if(gateways.isNullOrEmpty()){
            recycler_payments_gateway.visibility = View.GONE
            radio_visa.visibility = View.GONE
            isCash= true
        }
val  paymentGatewayAdapter = AvailablePaymentGatewayAdapter(gateways) {gatewayID ->
    selectedGatWay = gatewayID
}
        val  horizontalLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        recycler_payments_gateway.layoutManager = horizontalLayoutManager
        recycler_payments_gateway.adapter = paymentGatewayAdapter
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false)
    }
    private fun setupUI(){
        val  verticalLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recycler_checkout_products.layoutManager = verticalLayoutManager
        cartAdapter =
            OrderCheckoutAdapter(
                products
            )

        recycler_checkout_products.adapter = cartAdapter
        val view:TextView = requireActivity().findViewById<View>(R.id.text_carts_bar_title) as TextView
        val barTitle = getString(R.string.checkout_title)
        view.text = barTitle
        layout_to_checkout.setOnClickListener {
            // todo payment total with shipmment
            // request to server
            testProductsAvailabilityInStore()

        }

    }
    private fun testProductsAvailabilityInStore(){
        val sessionManager = SessionManager(requireContext())
        val userToken = sessionManager.fetchAuthToken()
        val adapter = AvailableProductsAdapter()
        if (!isCash && selectedGatWay == null){
            Toasty.warning(requireContext(),getString(R.string.select_gateway),Toasty.LENGTH_LONG).show()
            return
        }
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
             testAvailbleProductsCall = apiInterface.testProductAvailability(
                AvailableProductsQuantityRequest(availableProductsQuantity),token)
            loading.visibility = View.VISIBLE
            testAvailbleProductsCall!!.enqueue(object : Callback<AvailableProductsResponse> {
                override fun onFailure(call: Call<AvailableProductsResponse>, t: Throwable) {
                    val message = t.message
                    if (message == "Canceled"){
                        return
                    }
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
    private fun compareProductsWithStoreAvailability(storeProducts:ArrayList<ProductAvailability>){
        var productsAvailabilityStatus = true
        storeProducts.forEach {storeProduct ->
            val productInCarts =  productsInCart?.filter { it.id ==storeProduct.productID }
            val productInCart = productInCarts!!.first()
            productInCart.totalAvailbilty =storeProduct.availability
            if (productInCart.totalAvailbilty!! < productInCart.quantity){
                if(productsInCart!!.contains(productInCart)){
                    productsInCart!!.remove(productInCart)
                    productsInCart!!.add(productInCart)
                    productsAvailabilityStatus = false
                }
            }
            val index = productsInCart!!.indexOf(productInCart)
            if (productsInCart!!.size >= index){
                cartAdapter.notifyDataSetChanged()
            }
        }
        if (productsAvailabilityStatus){
            performCheckout()
        }else {
            val cartManger = CartsManger(requireContext())
            cartManger.updateCarts(productsInCart!!)
            backToCarts()
        }
    }
    private fun performCheckout(){
        val defaultAddressManger = AddressManger(requireContext())
        val defaultAddress = defaultAddressManger.getDefaultAddress()
        if (defaultAddress == null){
            Toasty.warning(requireContext(),"Please Select Your address",Toasty.LENGTH_LONG).show()
            return
        }
        if (!isCash && selectedGatWay == null){
            Toasty.warning(requireContext(),getString(R.string.select_gateway),Toasty.LENGTH_LONG).show()
            return
        }
        val checkoutRequst = CheckoutRequest(addressBookID = defaultAddress.id,cashOnDelievry = isCash,productsInCart = productsInCart!!)
        val sessionManager = SessionManager(requireContext())
        val userToken = sessionManager.fetchAuthToken()
        if (userToken.isNullOrEmpty()){
            Toasty.error(requireContext(),"Please Login And First",Toasty.LENGTH_LONG).show()
        }else {
            val token = "Token " + userToken!!
            if(addressID != null){
                val checkoutRequst = CheckoutRequest(addressBookID = addressID!!,cashOnDelievry = isCash,productsInCart = productsInCart!!)

                val checkoutCall = apiInterface.porceedToCheckout(checkoutRequst,token)
                loading.visibility = View.VISIBLE
                checkoutCall.enqueue(object :Callback<CheckoutResponse>{
                    @SuppressLint("StringFormatInvalid")
                    override fun onFailure(call: Call<CheckoutResponse>, t: Throwable) {
                        Toasty.error(requireContext(),getString(R.string.please_check_internet_connection,Toasty.LENGTH_LONG,true)).show()
                        call.cancel()
                        loading.visibility = View.GONE

                    }

                    override fun onResponse(
                        call: Call<CheckoutResponse>,
                        response: Response<CheckoutResponse>
                    ) {
                        loading.visibility = View.GONE

                        val erroBody = response.errorBody().toString()
                        val checkoutResponse: CheckoutResponse? = response.body()
                        if (response.code() == 200) {
                            if (checkoutResponse?.checkoutData?.shippingCost != null) {
                                checkoutResponse?.checkoutData?.let {
                                    setShippingCost(it)
                                }
                            }else {
                                Toasty.warning(requireContext(),checkoutResponse?.message!!,Toasty.LENGTH_LONG,true).show()
                            }

                        }


                    }

                })
            }else{
                Toasty.warning(requireContext(),"Please Select Address",Toasty.LENGTH_LONG).show()


            }



        }
    }
    private fun backToCarts(){
        val fragment = CartsFragment.newInstance()
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.popFragment(fragment)
        fc?.replaceFragment(fragment)
    }
    private fun getDefaultAddress(){
    val addressManger = AddressManger(requireContext())

        val defaultAddress = addressManger.getDefaultAddress()

        val userAccountManger = UserAccountManger(requireContext())
        val user = userAccountManger.getAccountUSer()

        text_buyer_name_checkout.text = user?.fullName
        if (defaultAddress != null) {
            addressID = defaultAddress.id
            text_location_checkout.text = defaultAddress.streetAddress
            text_city_checkout.text = defaultAddress.city.name
            text_country_checkout.text = defaultAddress.country.name
            text_no_default_address.visibility = View.GONE
        } else{
            text_no_default_address.visibility = View.VISIBLE
            text_country_checkout.visibility = View.GONE
            text_location_checkout.visibility = View.GONE
            text_city_checkout.visibility = View.GONE
            text_no_default_address.setOnClickListener{
                navigateToMyAddressBook()
            }

        }
    }
    override fun onPause() {
        super.onPause()
        if (testAvailbleProductsCall!=null)
            testAvailbleProductsCall!!.cancel()
    }
private fun setShippingCost(checkoutData:CheckoutData){
    val shippoingCost = checkoutData.shippingCost.toString()
    val totalCost = checkoutData.totalCost.toString()
    val orderCost = checkoutData.orderCost
    val arrivalDate = checkoutData.arrivalDate
    val addressBookId = checkoutData.addressBookID
    navigateToConfirmPayment(totalCost = totalCost,shippingCost = shippoingCost,arriveDate = arrivalDate,addresBookID = addressBookId)

   val cartsManger = CartsManger(requireContext())
   // cartsManger.emptyCart()

}
    private fun navigateToConfirmPayment(totalCost:String,shippingCost:String,arriveDate:String?,addresBookID:Int){
        val fragment = ConfirmPaymentFragment.newInstance(totalCost = totalCost,shippinCost = shippingCost,shippingDate = arriveDate,addressBookID = addresBookID,isCash = isCash,selectedGateway = selectedGatWay)
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(fragment)
    }

    private fun navigateToMyAddressBook(){
        val fragment =  MyAdressBookFragment.newInstance(false)

        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(fragment)
    }
    private fun getCarts(){
      val cartManger = CartsManger(requireContext())
        productsInCart = cartManger.getAllProducts()
        //this for bottomSheet
    /*    val firstProduct = productsInCart.elementAt(0)
        first_text_product_name_checkout.text = firstProduct.name
        first_text_product_price_checkout.text = firstProduct.price.toString()
        Glide.with(first_image_product_checkout).load(firstProduct.main_img).into(first_image_product_checkout);*/
        if (productsInCart != null){
            products = productsInCart!!
            val priceOfeachProduct = products.map {
                it.quantity * it.price
            }
            val discountOfEachProduct = products.map {
                if(it.afterPrice !=null){
                    it.quantity * it.afterPrice
                }else {
                    it.quantity * it.price
                }
            }
            val totalPrice = priceOfeachProduct.sum()
            val totalDicount = discountOfEachProduct.sum()
            if (totalPrice > totalDicount){
                // make discount visible
                line_offer_change.visibility  = View.VISIBLE
                text_total_discount.visibility = View.VISIBLE
                text_total_discount.text = totalDicount.toString()
            }
        val grandTotal = totalPrice.toString()
            text_payment_value.text = grandTotal
            cartAdapter.addItems(products)
            cartAdapter.notifyDataSetChanged()

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance() = CheckoutFragment()

    }
}