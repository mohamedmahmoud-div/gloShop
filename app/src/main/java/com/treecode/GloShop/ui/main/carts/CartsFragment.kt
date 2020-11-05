package com.treecode.GloShop.ui.main.carts

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.checkout.*
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.ui.adapter.carts.CartItemChanged
import com.treecode.GloShop.ui.adapter.carts.MyCartAdapter
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.search.ProductDetailsFragment
import com.treecode.GloShop.util.CartsManger
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.SessionManager
import com.example.mvvmcoorutines.data.api.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_carts.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartsFragment : Fragment() , CartItemChanged {

    private  var testAvailbleProductsCall: Call<AvailableProductsResponse>? = null
    private lateinit var cartsViewModel: CartsViewModel
    private lateinit var cartAdapter : MyCartAdapter
    private var products = HashSet<CartProduct>()
    private var updatedProducts = HashSet<CartProduct>()
    companion  object {
        fun newInstance() = CartsFragment()
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        cartsViewModel =
                ViewModelProviders.of(this).get(CartsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_carts, container, false)
        return root
    }
    private fun setupUI(){
        val  verticalLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerview_myCart.layoutManager = verticalLayoutManager
        val view: TextView = requireActivity().findViewById<View>(R.id.text_carts_bar_title) as TextView
        val barTitle = getString(R.string.mycart)
        view.text = barTitle
        cartAdapter = MyCartAdapter(products,this)

        recyclerview_myCart.adapter = cartAdapter
        btn_proceed_checkout.setOnClickListener{
            val apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
            val adapter = AvailableProductsAdapter()
            var availableProductsQuantity = ArrayList<AvailableProductQuantity>()
                updatedProducts.forEach {
                    val productID = it.id
                        val productQuantity = adapter.convertCartProduct(it)
                        availableProductsQuantity.add(productQuantity)

                }
            val sessionManager = SessionManager(requireContext())
            val token = sessionManager.fetchAuthToken()
            if (!token.isNullOrEmpty()) {
                val tokenToSent = "Token $token"
                 testAvailbleProductsCall = apiInterface.testProductAvailability(
                    AvailableProductsQuantityRequest(availableProductsQuantity),tokenToSent
                )
                check_cart_loading.visibility  = View.VISIBLE
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
                        check_cart_loading.visibility  = View.GONE

                        call.cancel()
                    }

                    override fun onResponse(
                        call: Call<AvailableProductsResponse>,
                        response: Response<AvailableProductsResponse>
                    ) {
                        check_cart_loading.visibility  = View.GONE

                        if (response.code() == 200) {
                            val availableResponse = response.body()
                            val availProductData = availableResponse!!.data
                            val prdocuts = availProductData.products
                            if (prdocuts.isNullOrEmpty()) {
                                // all products out of Stock
                            } else {

                               compareProductsWithStoreAvailability(prdocuts)
                            }
                        }else {
                            Toasty.warning(
                                requireContext(),
                                getString(R.string.please_check_login),
                                Toasty.LENGTH_LONG
                            ).show()
                        }

                    }

                })
            }else{
                Toasty.warning(
                    requireContext(),
                    getString(R.string.please_check_login),
                    Toasty.LENGTH_LONG
                ).show()
            }
        }


    }
    private fun compareProductsWithStoreAvailability(storeProducts:ArrayList<ProductAvailability>){
        var productsAvailabilityStatus = true
        storeProducts.forEach {storeProduct ->
          val productInCarts =  updatedProducts.filter { it.id ==storeProduct.productID }
            val productInCart = productInCarts.first()
            productInCart.totalAvailbilty =storeProduct.availability
            if (productInCart.totalAvailbilty!! < productInCart.quantity)
                productsAvailabilityStatus = false
            val index = updatedProducts.indexOf(productInCart)
            if (updatedProducts.size >= index){
                onQuantityChanged(productInCart)
                cartAdapter.notifyDataSetChanged()
            }
        }
    if (productsAvailabilityStatus){
        updateCart(updatedProducts)
        navigateToCheckoutFragment()
    }
    }
    private fun updateCart(products: HashSet<CartProduct>){
        val gson = Gson()

        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Cart, "")

        val type = object : TypeToken<Set<CartProduct>>(){}.type

        val jsonToSave = gson.toJson(products)
        prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
        prefsEditor.apply()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            setupUI()

            getCarts()

    }

    override fun onStop() {
        super.onStop()

       // products.clear()
    }
    private fun navigateToCheckoutFragment(){
        val fragment = CheckoutFragment.newInstance()
        val args = Bundle()
        //args.putInt(Constants.BUNDLE_COLLECTION_ID, collectionId)
        fragment.arguments = args

        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.addFragment(fragment)
    }
    private fun getCarts(){
        val cartManger = CartsManger(requireContext())
val productsInCart = cartManger.getAllProducts()
        if (!productsInCart.isNullOrEmpty()){
            updatedProducts = productsInCart!!
            showViews()
            updatedProducts = productsInCart
            products = productsInCart
            val listOfPrices = products.map {
                if (it.afterPrice == null)
                it.quantity * it.price
                else
                    it.quantity  * it.afterPrice
            }
            val totalPrice = listOfPrices.sum()
            text_checkout_total.text = totalPrice.toString()

            if (cartAdapter.products.size == 0){
                cartAdapter.addItems(products)
                cartAdapter.notifyDataSetChanged()
            }



        }else if (productsInCart.isNullOrEmpty()){
            showNoItemsInCart()
        }

    }
    override fun onQuantityChanged(product: CartProduct) {
        if(updatedProducts.contains(product)){
            updatedProducts.remove(product)
            updatedProducts.add(product)
            val listOfPrices = updatedProducts.map {
            if (it.afterPrice == null)
                it.quantity * it.price
            else
                it.quantity  * it.afterPrice
            }
            val totalPrice = listOfPrices.sum()
            text_checkout_total.text = totalPrice.toString()

        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemDelete(product: CartProduct) {
            updatedProducts.removeIf { it.id == product.id }
            if (updatedProducts.size == 0)
                showNoItemsInCart()

        updateCart(updatedProducts)
    }

    override fun onItemClickToView(product: CartProduct) {
        navigateToProductScreen(product)
    }
    private fun navigateToProductScreen(prodcut: CartProduct){
        val fragment = ProductDetailsFragment.newInstance()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_TOPDEAL_ID, prodcut.id)
        fragment.arguments = args

        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.addFragment(fragment)

    }

    override fun onPause() {
        super.onPause()
        if (testAvailbleProductsCall!=null)
        testAvailbleProductsCall!!.cancel()
        updateCart(updatedProducts)
    }
    fun showNoItemsInCart(){
        text_carts_empty.visibility = View.VISIBLE
        text_carts_empty_fill_message.visibility = View.VISIBLE
        image_error.visibility = View.VISIBLE
        recyclerview_myCart.visibility = View.GONE
        text_checkout_total_header.visibility = View.GONE
        text_cart_le.visibility = View.GONE
        layout_to_checkout.visibility = View.GONE
    }
    fun showViews(){
        text_carts_empty.visibility = View.GONE
        image_error.visibility = View.GONE
        text_carts_empty_fill_message.visibility = View.GONE

        recyclerview_myCart.visibility = View.VISIBLE
        text_checkout_total_header.visibility = View.VISIBLE
        text_cart_le.visibility = View.VISIBLE
        layout_to_checkout.visibility = View.VISIBLE
    }
}