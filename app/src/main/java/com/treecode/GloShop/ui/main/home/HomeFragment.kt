package com.treecode.GloShop.ui.main.home

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.Cateogry
import com.treecode.GloShop.data.model.HotDeal
import com.treecode.GloShop.data.model.home.*
import com.treecode.GloShop.ui.adapter.HorizontalScrollManger
import com.treecode.GloShop.ui.adapter.HotDealsAdapter
import com.treecode.GloShop.ui.adapter.NewArrivalAdapter
import com.treecode.GloShop.ui.adapter.RecyclerViewCallback
import com.treecode.GloShop.ui.adapter.home.CateogiesAdapter
import com.treecode.GloShop.ui.adapter.home.DepartmentAdapter
import com.treecode.GloShop.ui.adapter.home.ProductsOfferAdapter
import com.treecode.GloShop.ui.main.BaseActivity
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.home.ui.main.HomeActivity
import com.treecode.GloShop.ui.main.search.ProductDetailsFragment
import com.treecode.GloShop.ui.main.search.SearchFragment
import com.treecode.GloShop.ui.viewmodel.HomeViewModel
import com.treecode.GloShop.ui.viewmodel.ViewModelFactory
import com.treecode.GloShop.util.CartsManger
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.MyWishManger
import com.treecode.GloShop.util.hideKeyboard
import com.example.mvvmcoorutines.data.api.ApiHelper
import com.example.mvvmcoorutines.util.Status
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_product_details.*


class HomeFragment : Fragment() , RecyclerViewCallback {

    private lateinit var gridManager: GridLayoutManager
    private lateinit var handler: Handler
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var dealsAdapter:HotDealsAdapter

    private lateinit var categoryAdapter : CateogiesAdapter
    private lateinit var newArrivalAdapter :NewArrivalAdapter
    private lateinit var newArrivalDepartmentAdapter: DepartmentAdapter
    private lateinit var topSellingAdapter : NewArrivalAdapter
    private lateinit var topSellingDepartmentAdapter: DepartmentAdapter
    private lateinit var productOfferAdapter: ProductsOfferAdapter
    private var deaList = ArrayList<HotDeal>()
    private var categoryList = ArrayList<Cateogry>()
    private var newArrivalDepartments = ArrayList<Department>()
    private var topSellingDepartments = ArrayList<Department>()
    private var arrivalsList = ArrayList<Product>()
    private var topSellingList = ArrayList<Product>()
    private var productsOffer = ArrayList<Offer>()
    private lateinit var dummyCategory :Cateogry

    var navController: NavController? = null
    companion object {
        fun newInstance() = HomeFragment()
        var collectionWidth = 0

    }
    // TODO: 8/16/2020 MAKE shimmer layout for all sections

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        setupViewModels()
        val homeActivit =    requireContext() as BaseActivity

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //navController = Navigation.findNavController(view)

        setupSearch()
    }
private fun setupViewModels(){
    homeViewModel = ViewModelProviders.of(
        this,
        ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
    ).get(HomeViewModel::class.java)
}
    private fun renderNewArrival(){
    }
    private fun retrieveList(homeResponse: Home) {
        // TODO: 8/16/2020 each Adapter apply own list of the home list of inner proprties
//dealsAdatper , Cateogry Adapter , New Arrival Adapter , Top Selling Adapter
     val homeActivit =    requireContext() as BaseActivity
        BaseActivity.home = homeResponse
if (homeResponse.hot_deals.isNullOrEmpty() && homeResponse.collections.isNullOrEmpty() && homeResponse.new_arrivals.isNullOrEmpty()&&homeResponse.top_selling.isNullOrEmpty() && homeResponse.productsOffer.isNullOrEmpty()) {
    layout_no_items.visibility = View.VISIBLE
    text_deal_header.visibility = View.GONE
    recyclerview_deal_horizontal.visibility = View.GONE
    text_collection_header.visibility = View.GONE
    recyclerview_collections_horizontal.visibility = View.GONE
    text_arrival_header.visibility = View.GONE
    recyclerview_arrivals_department.visibility = View.GONE
    recyclerview_arrivals_horizontal.visibility = View.GONE
    text_top_selling_header.visibility = View.GONE
    recyclerview_top_selling_department.visibility = View.GONE
    recyclerview_top_selling_horizontal.visibility = View.GONE
    recyclerview_offer_product_horizontal.visibility = View.GONE
    text_offer_product_header.visibility = View.GONE
    return
} else {
    layout_no_items.visibility = View.GONE

}
        deaList = homeResponse.hot_deals
        if (deaList.size == 0){
            text_deal_header.visibility = View.GONE
            recyclerview_deal_horizontal.visibility = View.GONE
        } else {
            text_deal_header.visibility = View.VISIBLE
            recyclerview_deal_horizontal.visibility = View.VISIBLE
        }
        dealsAdapter.addDeals(deaList)
        dealsAdapter.notifyDataSetChanged()

         handler = Handler(Looper.getMainLooper())

        val runnable: Runnable = object : Runnable {
            var count = 0
            var flag = true
            override fun run() {
                if (count < dealsAdapter.getItemCount()) {
                    if (count == dealsAdapter.getItemCount() - 1) {
                        flag = false
                    } else if (count == 0) {
                        flag = true
                    }
                    if (flag) count++ else count--
                    try {
                        recyclerview_deal_horizontal.smoothScrollToPosition(count)
                        handler.postDelayed(this, 4000)
                    }catch (e:Exception){
                        handler.removeCallbacks(this)

                    }

                }
            }
        }
        handler.postDelayed(runnable,4000)

        categoryList = homeResponse.collections
        if (categoryList.isNullOrEmpty()) {
            text_collection_header.visibility = View.GONE
            recyclerview_collections_horizontal.visibility = View.GONE
        }else {
            text_collection_header.visibility = View.VISIBLE
            recyclerview_collections_horizontal.visibility = View.VISIBLE
        }
        categoryAdapter.addCategoryies(categoryList)
        categoryAdapter.notifyDataSetChanged()





        val listOfProductInCart =  getCarts()
        val listOfProductInWishList = getWish()

        newArrivalDepartments = homeResponse.new_arrivals
        if (newArrivalDepartments.isNullOrEmpty()){
            text_arrival_header.visibility = View.GONE
            recyclerview_arrivals_department.visibility = View.GONE
            recyclerview_arrivals_horizontal.visibility = View.GONE
        }else{
            text_arrival_header.visibility = View.VISIBLE
            recyclerview_arrivals_department.visibility = View.VISIBLE
            recyclerview_arrivals_horizontal.visibility = View.VISIBLE

            newArrivalDepartmentAdapter.addDepartments(newArrivalDepartments)
            newArrivalDepartmentAdapter.notifyDataSetChanged()

            arrivalsList = newArrivalDepartments[0].products
            listOfProductInCart.forEach { cartID ->
                arrivalsList.forEach {product ->
                    if (product.id == cartID){
                        product.isCartSelected = true
                    }
                }

            }
            listOfProductInWishList.forEach { wishID ->
                arrivalsList.forEach{product ->
                    if (product.id == wishID){
                        product.isWishSelected = true
                    }else{
                        product.isWishSelected = false
                    }
                }

            }
            newArrivalAdapter.addNewItems(arrivalsList)
            newArrivalAdapter.notifyDataSetChanged()
        }








        topSellingDepartments = homeResponse.top_selling
        if (topSellingDepartments.isNullOrEmpty()) {
            text_top_selling_header.visibility = View.GONE
            recyclerview_top_selling_department.visibility = View.GONE
            recyclerview_top_selling_horizontal.visibility = View.GONE
        }else {
            text_top_selling_header.visibility = View.VISIBLE
            recyclerview_top_selling_department.visibility = View.VISIBLE
            recyclerview_top_selling_horizontal.visibility = View.VISIBLE
            topSellingDepartmentAdapter.addDepartments(topSellingDepartments)
            topSellingDepartmentAdapter.notifyDataSetChanged()

            topSellingList = topSellingDepartments[0].products

            topSellingList = newArrivalDepartments[0].products
            listOfProductInCart.forEach { cartID ->
                arrivalsList.forEach {product ->
                    if (product.id == cartID){
                        product.isCartSelected = true
                    }
                }

            }


            listOfProductInWishList.forEach { wishID ->
                topSellingList.forEach{product ->
                    if (product.id == wishID){
                        product.isWishSelected = true
                    }else{
                        product.isWishSelected = false
                    }
                }

            }



            topSellingAdapter.addNewItems(topSellingList)
            topSellingAdapter.notifyDataSetChanged()
        }

        productsOffer = homeResponse.productsOffer
        if(productsOffer.isNullOrEmpty()){
            recyclerview_offer_product_horizontal.visibility = View.GONE
            text_offer_product_header.visibility = View.GONE
        }else{
            productOfferAdapter.addNewItems(productsOffer)
            productOfferAdapter.notifyDataSetChanged()
            recyclerview_offer_product_horizontal.visibility = View.VISIBLE
            text_offer_product_header.visibility = View.VISIBLE
        }
    }

@SuppressLint("StringFormatInvalid")
private fun setupObserver(){
    homeViewModel
    homeViewModel.getHome().observe(viewLifecycleOwner, Observer {
        it?.let { resource ->
            when (resource.status) {
                Status.SUCCESS -> {
                    shimmerFrameLayout.stopShimmerAnimation()
                    shimmerFrameLayout.visibility = View.GONE
                    resource.data?.let { homeData -> retrieveList(homeData.data!!) }

                    btn_add_cart_product_details.setOnClickListener {
                        this.addToCartClicked()
                    }
                 //   recyclerView.visibility = View.VISIBLE
                  //  progressBar.visibility = View.GONE

                }
                Status.ERROR -> {
                    shimmerFrameLayout.stopShimmerAnimation()
                    shimmerFrameLayout.visibility = View.VISIBLE
                    Toasty.error(requireContext(),getString(R.string.please_check_internet_connection,Toasty.LENGTH_LONG)).show()
                    resource.data
                }
                Status.LOADING -> {
                    shimmerFrameLayout.startShimmerAnimation()
                    shimmerFrameLayout.visibility = View.VISIBLE
                 ///   progressBar.visibility = View.VISIBLE
                  //  recyclerView.visibility = View.GONE

                }
            }
        }
    })

}
private fun addToCartClicked(){

}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        setupUI()
        if  (BaseActivity.home != null) {
            shimmerFrameLayout.visibility = View.GONE
            retrieveList(BaseActivity.home!!)
        }else{
            setupObserver()
        }
        shimmerFrameLayout.startShimmerAnimation()
        swipe_refresh_home.setOnRefreshListener {
            setupObserver()
            swipe_refresh_home.setRefreshing(false); // Disables the refresh icon
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
    }
private fun setupSearch(){
  //  val searchItem = menu.findItem(R.id.action_search)
    val searchView = requireActivity().findViewById(R.id.search_searchvc) as SearchView

    searchView.queryHint = getString(R.string.search)
    searchView.findViewById<AutoCompleteTextView>(R.id.search_src_text).threshold = 1

    val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
    val to = intArrayOf(R.id.item_label)
    val cursorAdapter = androidx.cursoradapter.widget.SimpleCursorAdapter(
        context,
        R.layout.search_item,
        null,
        from,
        to,
        androidx.cursoradapter.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
    )
    val suggestions = listOf("Apple", "Blueberry", "Carrot", "Daikon")

    searchView.suggestionsAdapter = cursorAdapter

    searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            hideKeyboard()
            return false
        }

        override fun onQueryTextChange(query: String?): Boolean {
         val fragment = SearchFragment.newInstance()
            if (!query.isNullOrEmpty()){
                val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
                fc?.replaceFragment(fragment)
            }


           /* val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
            query?.let {
                suggestions.forEachIndexed { index, suggestion ->
                    if (suggestion.contains(query, true))
                        cursor.addRow(arrayOf(index, suggestion))
                }
            }

            cursorAdapter.changeCursor(cursor)*/
            return true
        }
    })

    searchView.setOnSuggestionListener(object: androidx.appcompat.widget.SearchView.OnSuggestionListener {
        override fun onSuggestionSelect(position: Int): Boolean {
            return false
        }

        override fun onSuggestionClick(position: Int): Boolean {
            hideKeyboard()
            val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
            val selection = cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
            searchView.setQuery(selection, false)

            // Do something with selection
            return true
        }

    })
}
    private fun navigateToCollectionSearchFragment(collectionId:Int){
        val fragment = SearchFragment.newInstance()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_COLLECTION_ID, collectionId)
        fragment.arguments = args

        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
      fc?.replaceFragment(fragment)
    }

    private fun navigateToTopDealSearchFragment(topDeal:Int){
        val fragment = SearchFragment.newInstance()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_TOPDEAL_ID, topDeal)
        fragment.arguments = args

        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(fragment)
    }


    private fun  setupUI(){
        val  horizontalLayoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        val customScrollManger = HorizontalScrollManger(this.context,LinearLayoutManager.HORIZONTAL,false)
recyclerview_deal_horizontal.layoutManager = customScrollManger
        dealsAdapter = HotDealsAdapter(deaList){topDealID ->
            navigateToTopDealSearchFragment(topDealID)

        }

        recyclerview_deal_horizontal.adapter = dealsAdapter

        var count = 0
        var flag = true;
        val speedScroll = 1200

         gridManager = GridLayoutManager(this.context, 2,GridLayoutManager.VERTICAL,false)

        recyclerview_collections_horizontal.layoutManager =  gridManager
            //LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        categoryAdapter =
            CateogiesAdapter(
                categoryList
            ) { collectionID ->

                navigateToCollectionSearchFragment(collectionID)
            }
recyclerview_collections_horizontal.adapter = categoryAdapter


        recyclerview_arrivals_horizontal.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
newArrivalAdapter = NewArrivalAdapter(arrivalsList,this) {product ->
    newArrivalAdapter.setOnCallbackListener(this)
    this.navigateToProductScreen(product)

}
        recyclerview_arrivals_horizontal.adapter = newArrivalAdapter

        recyclerview_arrivals_department.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        newArrivalDepartmentAdapter =
            DepartmentAdapter(
                newArrivalDepartments
            ) { productsList ->
                arrivalsList = productsList


                val listOfProductInCart = getCarts()
                val listOfProductInWishList = getWish()

                listOfProductInCart.forEach { cartID ->
                    arrivalsList.forEach { product ->
                        if (product.id == cartID) {
                            product.isCartSelected = true
                        } else {
                            product.isCartSelected = false
                        }
                    }

                }


                listOfProductInWishList.forEach { wishID ->
                    arrivalsList.forEach { product ->
                        if (product.id == wishID) {
                            product.isWishSelected = true
                        } else {
                            product.isWishSelected = false
                        }
                    }

                }
                newArrivalAdapter.replaceArrivals(arrivalsList)
                newArrivalAdapter.notifyDataSetChanged()


                //      this.navigateToPrdouctListScreen(productsList)

            }
        recyclerview_arrivals_department.adapter =  newArrivalDepartmentAdapter


        recyclerview_top_selling_horizontal.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
         topSellingAdapter = NewArrivalAdapter(topSellingList,this){product ->
             Toast.makeText(this.context, product.name, Toast.LENGTH_SHORT).show()
             this.navigateToProductScreen(product)
         }
        recyclerview_top_selling_horizontal.adapter = topSellingAdapter



        recyclerview_top_selling_department.layoutManager = LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        topSellingDepartmentAdapter =
            DepartmentAdapter(
                topSellingDepartments
            ) { productsList ->
                topSellingList = productsList


                val listOfProductInCart = getCarts()
                val listOfProductInWishList = getWish()

                listOfProductInCart.forEach { cartID ->
                    topSellingList.forEach { product ->
                        product.isCartSelected = product.id == cartID
                    }

                }


                listOfProductInWishList.forEach { wishID ->
                    topSellingList.forEach { product ->
                        if (product.id == wishID) {
                            product.isWishSelected = true
                        } else {
                            product.isWishSelected = false
                        }
                    }

                }



                topSellingAdapter.replaceArrivals(topSellingList)
                topSellingAdapter.notifyDataSetChanged()

                //   this.navigateToPrdouctListScreen(productsList)

            }
        recyclerview_top_selling_department.adapter =  topSellingDepartmentAdapter

     val productsOfferLayoutManger=   LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        recyclerview_offer_product_horizontal.layoutManager = productsOfferLayoutManger
        productOfferAdapter = ProductsOfferAdapter(productsOffer,this) {
            navigateToProductScreen(it.product)
        }
        recyclerview_offer_product_horizontal.adapter = productOfferAdapter
    }
 /*   private fun navigateToProductWithOffer(offer: Offer){
        val fragment = ProductDetailsFragment.newInstance()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_TOPDEAL_ID, prodcut.id)
        fragment.arguments = args

        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(fragment)
    }*/
    private fun navigateToPrdouctListScreen(products: ArrayList<Product>){
        val bundle = bundleOf(
            "products" to products
        )
        val activity = activity as HomeActivity
activity.naviagateToSearch()
        navController?.navigate(
            R.id.action_navigation_home_to_navigation_search,
            bundle
        )
    }
    // TODO: 8/16/2020 navigate to product page with Selected product
    private fun navigateToProductScreen(prodcut: Product){
        val fragment = ProductDetailsFragment.newInstance()
        val args = Bundle()
        args.putInt(Constants.BUNDLE_TOPDEAL_ID, prodcut.id)
        fragment.arguments = args

        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(fragment)

    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRecycleViewCartClicked(product: Product, checked:Boolean) {
        val gson = Gson()

        /*  val mPrefs: SharedPreferences =
              requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
          val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
          val json = mPrefs.getString(Constants.sharedKey_Add_Cart, "")

          val type = object : TypeToken<Set<Product>>(){}.type*/
        //var productsInCart: HashSet<Product>? = gson.fromJson(json, type)

        val cartManger = CartsManger(requireContext())
        val productInCart = cartManger.adapterToProductInCart(product)
        var allInCart = cartManger.getAllProducts()
        if (!allInCart.isNullOrEmpty()){

            val items = allInCart.filter { it.id == product.id }
            if (items.isNullOrEmpty()){
                if (checked) {
                    if (productInCart.quantity == 0)
                        productInCart.quantity = 1

                    allInCart.add(productInCart)


                }


            } else{
                if (!checked) {
                    allInCart.removeIf { it.id == product.id }}
            }

//            allInCart.forEach { productInCart ->
//                if (productInCart.id == product.id) {
//                    if (!checked) {
//
//                        allInCart!!.remove(productInCart)
//                        cartManger.updateCarts(allInCart!!)
//                        /*  val jsonToSave = gson.toJson(allInCart)
//                          prefsEditor.putString(Constants.sharedKey_Add_Cart, jsonToSave)
//                          prefsEditor.apply()*/
//                        return
//                    }else {
//                        if (product.pieceCount ==0)
//                            product.pieceCount = 1
//                        if(!allInCart!!.contains(productInCart)){
//                            allInCart!!.add(productInCart)
//
//                        }
//                    }
//                }
//
//
//            }

            //var cart:ArrayList<Products> = productsInCart
        }else{
            product.pieceCount = 1
            allInCart = HashSet<CartProduct>()
            allInCart.add(productInCart)
        }
        cartManger.updateCarts(allInCart!!)
        /*   val jsonToSave = gson.toJson(allInCart)
           prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
           prefsEditor.apply()*/
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onRecycleViewLWishClicked(proudct: Product, checked: Boolean) {
        val gson = Gson()
        val wishManger = MyWishManger(requireContext())
        val productWish = wishManger.adapterToProductInCart(proudct)
        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Wish, "")

        val type = object : TypeToken<Set<CartProduct>>(){}.type
        var productsInWish = wishManger.getWishList()

        if (!productsInWish.isNullOrEmpty()){


            val items = productsInWish.filter { it.id == proudct.id }
            if (items.isNullOrEmpty()){
                if (checked) {


                    productsInWish.add(productWish)


                }


            } else{
                if (!checked) {
                    productsInWish.removeIf { it.id == proudct.id }}
            }



//            productsInWish.forEach { productInCart ->
//                if (productInCart.id == proudct.id){
//                    if (!checked){
//
//                        productsInWish!!.remove(productInCart)
//                    wishManger.updateCarts(productsInWish!!)
//                   return
//                    }else{
//                        if (proudct.pieceCount ==0)
//                            proudct.pieceCount = 1
//                   val items =  productsInWish!!.filter { it.id == proudct.id }
//                        if (items.isNullOrEmpty()){
//                            productsInWish!!.add(productWish)
//
//                        }
//
//                    }
//                }
//            }



            //var cart:ArrayList<Products> = productsInCart
        }else{
            proudct.pieceCount = 1

            productsInWish = HashSet<CartProduct>()
            productsInWish.add(productWish)
        }
        wishManger.updateCarts(productsInWish!!)

    }
    private fun getCarts(): ArrayList<Int>{
        val gson = Gson()
        var s :ArrayList<Int> = ArrayList()
        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Cart, "")

        val type = object : TypeToken<Set<Product>>(){}.type
        var productsInCart: HashSet<Product>? = gson.fromJson(json, type)

        if (productsInCart != null){
            s =    productsInCart.map {
                it.id!!
            } as ArrayList<Int>

        }

        return s
    }

    private fun  getWish():ArrayList<Int>{
        val gson = Gson()
        var wishlistInt:ArrayList<Int> = ArrayList()
        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Wish, "")

        val type = object : TypeToken<Set<Product>>(){}.type
        var productsInCart: HashSet<Product>? = gson.fromJson(json, type)
        if (productsInCart != null){
            wishlistInt = productsInCart.map {
                it.id!!
            } as ArrayList<Int>
        }

        return wishlistInt
    }
    override fun onStop() {
        super.onStop()
        //handler.removeCallbacks()
        try {
            handler.removeCallbacks {
            }
        }catch (e:java.lang.Exception){

        }

    }

}



