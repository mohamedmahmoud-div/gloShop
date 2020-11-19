package com.treecode.GloShop.ui.main.search

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.data.model.search.*
import com.treecode.GloShop.ui.adapter.NewArrivalAdapter
import com.treecode.GloShop.ui.adapter.RecyclerViewCallback
import com.treecode.GloShop.ui.adapter.productdetails.ProductDetailsImageAdapter
import com.treecode.GloShop.ui.adapter.productdetails.ProductReviewAdapter
import com.treecode.GloShop.ui.adapter.productspec.AllSpecsChangeListener
import com.treecode.GloShop.ui.adapter.productspec.ProductColorSpecAdapter
import com.treecode.GloShop.ui.adapter.productspec.ProductsAllSpecAdapter
import com.treecode.GloShop.ui.customviews.MultiSwipeRefreshLayout
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.util.CartsManger
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.MyWishManger
import com.example.mvvmcoorutines.data.api.ApiService
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.treecode.GloShop.ui.adapter.productdetails.ProductImageClickListener
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductDetailsFragment : Fragment(),RecyclerViewCallback ,AllSpecsChangeListener,
    ProductImageClickListener {
    private var swipeRefreshLayout: MultiSwipeRefreshLayout? =null
    private lateinit var productDetails: ProductDetailsData

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var apiInterface: ApiService
    private var productListOfImages = ArrayList<String>()
    private lateinit var productImageAdapter : ProductDetailsImageAdapter
    private lateinit var colorsSpecAdapter: ProductColorSpecAdapter
    private lateinit var reviewsAdapter:ProductReviewAdapter
    private lateinit var allSpecsAdapter:ProductsAllSpecAdapter
    private lateinit var relatedProductsAdapter : NewArrivalAdapter
    private var allSpecs = ArrayList<Specification>()
    private var productReviews = ArrayList<ProductReview>()
    private var relatedProducts = ArrayList<Product>()
    private var colorSpecs = ArrayList<SpecValue>()
    private var textSpecs = ArrayList<SpecValue>()
    private var selectedColor:Int? = null
    lateinit  var productCart :CartProduct
    private var selectedSpecs = ArrayList<SelectedSpec>()
    private val dicOfSelectedSepc = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    var totalQuantity :Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductDetailsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance() = ProductDetailsFragment()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerProductFrameLayout.startShimmerAnimation()
        setupViews()
        val searchView = requireActivity().findViewById(R.id.search_searchvc) as SearchView?
        if (searchView != null) {
            searchView.visibility = View.GONE
        }
        swipeRefreshLayout =   view.findViewById<MultiSwipeRefreshLayout>(R.id.swipe_refresh_product)
        swipeRefreshLayout!!.setSwipeableChildren(scrollable_product.id)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
            val args = arguments
            swipe_refresh_product.setOnRefreshListener {
                swipe_refresh_search.isRefreshing = false
            }
            var productId:Int? = args!!.getInt(Constants.BUNDLE_TOPDEAL_ID)
            productId?.let { it1 ->
                setupCartCount()
                isProductInCart(it1)
                getProductFromNetwork(it1)
            }
            swipe_refresh_product.setOnRefreshListener {
                swipe_refresh_product.isRefreshing = false
                productId?.let { it1 ->
                    setupCartCount()
                    isProductInCart(it1)
                    getProductFromNetwork(it1)
                }}
        }
    }
    private fun setupCartCount(){
        val cartsManger = CartsManger(requireContext())
        val cart = cartsManger.getAllProducts()
        if (!cart.isNullOrEmpty()){
            button_cart_count.visibility = View.VISIBLE
            button_cart_count.text = "${cart.size}"
        }
    }
    private fun setupViews(){
        val  horizontalLayoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.HORIZONTAL,true)
        horizontalLayoutManager.isSmoothScrollbarEnabled = true

        recyclerview_product_details_images.layoutManager = horizontalLayoutManager
        productImageAdapter = ProductDetailsImageAdapter(productListOfImages,this)
        recyclerview_product_details_images.adapter = productImageAdapter
        val specsLayoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.VERTICAL,false)
        recyclerview_product_spec_change.layoutManager = specsLayoutManager
        allSpecsAdapter = ProductsAllSpecAdapter(allSpecs,this)
        recyclerview_product_spec_change.adapter = allSpecsAdapter

        val reviewsLayoutManger =  LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        recycler_reviews.layoutManager = reviewsLayoutManger
        reviewsAdapter = ProductReviewAdapter(productReviews)
        recycler_reviews.adapter = reviewsAdapter

        recyclerview_product_related_product.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        relatedProductsAdapter = NewArrivalAdapter(relatedProducts,this){ product ->
            this.navigateToProductScreen(product)
        }
        recyclerview_product_related_product.adapter = relatedProductsAdapter
        image_product_Zoom.setOnClickListener {
            image_product_Zoom.visibility = View.GONE
        }

    }
    private fun navigateToProductScreen(prodcut: Product){
        val fragment = ProductDetailsFragment.newInstance()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_TOPDEAL_ID, prodcut.id)
        fragment.arguments = args
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(fragment)

    }
    private fun isProductInCart(productId: Int){
        val cartsManger  = CartsManger(requireContext())
        var inCart = false
        val product = cartsManger.getProduct(productID = productId)
        if (product != null) {
            val  specification = product.specification
            btn_add_cart_product_details.setText(R.string.remove_cart)
            inCart = true
        }else {
            btn_add_cart_product_details.setText(R.string.add_cart)
            inCart = false

        }

        btn_add_cart_product_details.setOnClickListener {
            if (productDetails!= null){
                if (productDetails.specification != null && dicOfSelectedSepc.size ==0){
                    Toasty.warning(requireContext(),"Please Select specification ").show()
                }else {
                    dicOfSelectedSepc.map {
                        if (!productCart.seletectedSpecValuesString.contains(it.value))
                            if  (productCart.seletectedSpecValuesString != "")
                                productCart.seletectedSpecValuesString += ","+it.key + ":"+ it.value
                            else
                                productCart.seletectedSpecValuesString += it.key + ":"+ it.value
                    }
                }
            }
            Toasty.success(requireContext(),getString(R.string.added_to_cart),Toasty.LENGTH_LONG).show()
            productCart.totalAvailbilty = totalQuantity!!
            cartsManger.saveCart(productCart)
        }
    }
    fun<T> isEqual(first: List<T>, second: List<T>): Boolean {

        if (first.size != second.size) {
            return false
        }

        return first.zip(second).all { (x, y) -> x == y }
    }
    private fun getProductFromNetwork(productId:Int){
        val productDetailsCall = apiInterface.getProductDetails(productId)
        layou_product_details.visibility = View.GONE
        shimmerProductFrameLayout.visibility = View.VISIBLE
        shimmerProductFrameLayout.startShimmerAnimation()
        productDetailsCall.enqueue(object : Callback<ProductDetailsResponse?> {
            override fun onResponse(
                call: retrofit2.Call<ProductDetailsResponse?>,
                response: Response<ProductDetailsResponse?>
            ) {
                shimmerProductFrameLayout.stopShimmerAnimation()
                layout_container_shimmer.visibility = View.GONE
                layou_product_details.visibility  = View.VISIBLE
                text_product_internet_connection.visibility = View.GONE

                val erroBody = response.errorBody().toString()
                val header = response.headers()
                val loginResponse: ProductDetailsResponse? = response.body()

                val text: String? = loginResponse?.message
                if(response.code() == 200){
                    if (text != null && text == "") {
                        swipe_refresh_product.isRefreshing = false
                      swipeRefreshLayout!!.isEnabled   = false
                        retrieveList(loginResponse)
                    } else if (text != null) {
                        //  Toasty.error(applicationContext, "$text page", Toast.LENGTH_LONG, true).show();
                    }
                }else {
                    layout_container_shimmer.visibility = View.GONE
                    layou_product_details.visibility = View.GONE
                    text_product_internet_connection.visibility = View.VISIBLE
                    text_product_internet_connection.text = getString(R.string.out_of_stock)
                    layout_add_cart_product.visibility = View.GONE
                    swipeRefreshLayout!!.isEnabled = false

                    //call.cancel()
                }

            }

            override fun onFailure(
                call: retrofit2.Call<ProductDetailsResponse?>,
                t: Throwable
            ) {
                shimmerProductFrameLayout.stopShimmerAnimation()
                layout_container_shimmer.visibility = View.INVISIBLE
                layou_product_details.visibility = View.GONE
                text_product_internet_connection.visibility = View.VISIBLE
                layout_add_cart_product.visibility = View.GONE
                call.cancel()
            }
        })


    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    private fun retrieveList(productDetailsResponse: ProductDetailsResponse) {
//        swipe_refresh_product.isRefreshing = false
        layout_add_cart_product.visibility = View.VISIBLE

        productDetails = productDetailsResponse.data
        val cartsManger = CartsManger(requireContext())
        productCart = cartsManger.productAdapter(productDetails)
        productDetailsResponse.data.otherImages.forEach { otherImage ->
            productListOfImages.add(otherImage.image)
        }
        if(productDetails.specification != null){
            val allValuesQuantity = productDetails.specification?.values!!.map {
                it.quantity
            }
            totalQuantity = allValuesQuantity.sum()
            //allSpecs = ArrayList()
            allSpecs.add(productDetails.specification!!)
//            allSpecsAdapter.addSpecs(allSpecs)
            allSpecsAdapter.notifyDataSetChanged()
        }else {
            totalQuantity = productCart.totalAvailbilty
            recyclerview_product_spec_change.visibility = View.GONE
        }



/*val filterTextSpec = specs.filter {
    !it.isColor
} as ArrayList<Specification>
   textSpecs = filterTextSpec.map {
       it.values

   }   as ArrayList<SpecValue>*/
        productListOfImages.add(productDetailsResponse.data.img)
        // productImageAdapter.addImages(productListOfImages)
        productImageAdapter.notifyDataSetChanged()
        val reviews = productDetailsResponse.data.productReviews
        if (!reviews.isNullOrEmpty() ){
            reviews?.let {
                productReviews.addAll(it)
                reviewsAdapter.notifyDataSetChanged()
                recycler_reviews.visibility = View.VISIBLE
                text_no_reviews.visibility = View.GONE
            }
        }else{
            recycler_reviews.visibility = View.GONE
            text_no_reviews.visibility = View.VISIBLE
            val set = ConstraintSet()
            val constraintLayout = view?.findViewById<ConstraintLayout>(R.id.layou_product_details)
            set.clone(constraintLayout)
            set.connect(text__related_products.id,ConstraintSet.TOP,text_no_reviews.id,ConstraintSet.BOTTOM,16)
            set.applyTo(constraintLayout)

            //    text__related_products.constraintLayout.top
        }

        val priceAfterChange = productDetails.priceAfterDicount
        val dicount = productDetails.discount
        val offerType = productDetails.offerType
        if(priceAfterChange != null&& dicount!=null && offerType != null) {
            text_product_offer_type.visibility = View.VISIBLE

            if (dicount != 0){
                text_product_discount.visibility = View.VISIBLE
                text_price_after_product_deatails.visibility = View.VISIBLE
                line_offer_change.visibility = View.VISIBLE
            }
            text_product_offer_type.text = offerType
            text_price_after_product_deatails.text =  "$priceAfterChange ${getString(R.string.le)}"
            text_product_discount.text = "$dicount%"
        }else if (offerType != null){
            text_product_offer_type.visibility = View.VISIBLE
            text_product_offer_type.text = offerType
            text_product_offer_type.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_rate))
        }
        text_product_name_details_.text = productDetails.name
        text_value_description.text = productDetails.description
        text_value_category.text = productDetails.category.name
        text_price_product_details.text = productDetails.price.toString()
        text_rating_percent.text = productDetails.reviews.toString()
        relatedProducts  = productDetails.relatedProducts
        relatedProductsAdapter.addNewItems(relatedProducts)
        relatedProductsAdapter.notifyDataSetChanged()

    }

    override fun onRecycleViewCartClicked(product: Product,checked: Boolean) {
        val gson = Gson()

        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Cart, "")

        val type = object : TypeToken<Set<Product>>(){}.type
        //var productsInCart: HashSet<Product>? = gson.fromJson(json, type)

        val cartManger = CartsManger(requireContext())
        val productInCart = cartManger.adapterToProductInCart(product)
        var allInCart = cartManger.getAllProducts()
        if (allInCart != null){
            allInCart.forEach { productInCart ->
                if (productInCart.id == product.id) {
                    if (!checked) {

                        allInCart!!.remove(productInCart)
                        cartManger.updateCarts(allInCart!!)
                        /*  val jsonToSave = gson.toJson(allInCart)
                          prefsEditor.putString(Constants.sharedKey_Add_Cart, jsonToSave)
                          prefsEditor.apply()*/
                        return
                    }
                }


            }
            if (product.pieceCount ==0)
                product.pieceCount = 1
            if(!allInCart.contains(productInCart)){
                allInCart.add(productInCart)

            }

            //var cart:ArrayList<Products> = productsInCart
        }else{
            product.pieceCount = 1
            allInCart = HashSet<CartProduct>()
            allInCart.add(productInCart)
        }
        cartManger.updateCarts(allInCart!!)    }

    override fun onRecycleViewLWishClicked(product: Product,checked: Boolean) {
        val gson = Gson()
        val wishManger = MyWishManger(requireContext())
        val productWish = wishManger.adapterToProductInCart(product)
        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Wish, "")

        val type = object : TypeToken<Set<CartProduct>>(){}.type
        var productsInCart = wishManger.getWishList()

        if (productsInCart != null){
            productsInCart.forEach { productInCart ->
                if (productInCart.id == product.id){
                    if (!checked){

                        productsInCart!!.remove(productInCart)
                        wishManger.updateCarts(productsInCart!!)
                        return}
                }


            }
            if (product.pieceCount ==0)
                product.pieceCount = 1
            if(!productsInCart.contains(productWish)){
                productsInCart.add(productWish)

            }


            //var cart:ArrayList<Products> = productsInCart
        }else{
            product.pieceCount = 1

            productsInCart = HashSet<CartProduct>()
            productsInCart.add(productWish)
        }
        wishManger.updateCarts(productsInCart!!)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onChange(spec: Specification, specValue:SpecValue) {
        totalQuantity = specValue.quantity

        val specID = spec.id
        selectedSpecs.forEach {
            if(it.id == specID){

                selectedSpecs.remove(it)
                allSpecs.removeIf { it.id != specID && it.id> specID}
                allSpecsAdapter.notifyDataSetChanged()

                return@forEach
            }
        }

        selectedSpecs.add(SelectedSpec(id = specID,specValue = specValue.value,specChangeID = specValue.id))
        dicOfSelectedSepc.put(spec.name,specValue.value)


        productCart.selectedIDS = selectedSpecs
        if (!productCart.seletectedSpecValuesString.contains(specValue.value)){
            //productCart.seletectedSpecValuesString = productCart.seletectedSpecValuesString + "," +specValue.value
        }
        progress_circular_update_spec.visibility = View.VISIBLE

        val productDetailsCall = apiInterface.addNextSpecOfProductWith(specValue.id)

        productDetailsCall.enqueue(object : Callback<NextProductSpecResponse?> {
            override fun onResponse(
                call: retrofit2.Call<NextProductSpecResponse?>,
                response: Response<NextProductSpecResponse?>
            ) {
                progress_circular_update_spec.visibility = View.GONE
                val erroBody = response.errorBody().toString()
                val header = response.headers()
                val nextProductSpecResponse: NextProductSpecResponse? = response.body()
                if (nextProductSpecResponse?.data.isNullOrEmpty()){
                    return
                } else{
                    updateSpecs(nextProductSpecResponse!!.data)
                }
                val text: String? = nextProductSpecResponse?.message
                if (text != null && text == "") {
                } else if (text != null) {
                    //  Toasty.error(applicationContext, "$text page", Toast.LENGTH_LONG, true).show();
                }

            }

            override fun onFailure(
                call: retrofit2.Call<NextProductSpecResponse?>,
                t: Throwable
            ) {
                progress_circular_update_spec.visibility = View.GONE

                call.cancel()
            }
        })
    }
    private fun updateSpecs(newSpecs:ArrayList<Specification>){
        totalQuantity = newSpecs[0].values[0].quantity
        allSpecs.addAll(newSpecs)
        //allSpecsAdapter.addSpecs(newSpecs)
        allSpecsAdapter.notifyDataSetChanged()
    }

    override fun onImageSelected(path: String) {
        image_product_Zoom.visibility = View.VISIBLE
        Glide.with(requireContext()).load(path).into(image_product_Zoom);

    }
}
class SelectedSpec (
    var id:Int,
    var specChangeID:Int,
    var specValue:String
)
