package com.treecode.GloShop.ui.main.search

import android.app.SearchManager
import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.data.model.search.*
import com.treecode.GloShop.ui.adapter.ProductSearchAdapter
import com.treecode.GloShop.ui.adapter.RecyclerViewCallback
import com.treecode.GloShop.ui.adapter.search.FilterSpecificationAdapter
import com.treecode.GloShop.ui.main.BaseActivity
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.util.CartsManger
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.MyWishManger
import com.treecode.GloShop.util.hideKeyboard
import com.example.mvvmcoorutines.data.api.ApiService
import com.example.mvvmcoorutines.util.Status
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bottom_sheet_filter.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() , RecyclerViewCallback {

    private var call3: Call<SearchResponse>? = null
    private  var suggestions: List<String>? = null
    private  var appliedFilters = HashSet<SpecValue> ()
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchAdapter : ProductSearchAdapter
    private lateinit var filterAdapter : FilterSpecificationAdapter

    private var productList = ArrayList<Product>()
    private var filters = ArrayList<Specification>()
    private lateinit var apiInterface: ApiService
    private  var collection_id :Int? = null
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var activityHostID = Constants.Activty_Host_Search
    private var productListSearView = ArrayList<Product>()
    private var pageNumber = 1
    private var islastPage = false
    var colection:Int? = null
    var topDeal:Int? = null
    var searchText:String? = ""
    var isSearchLoading = false
    var itemsCount = 0
    var isAscending = true
    var productTextSearch = ""
    var loadWithRefresh = true
    var scrollToMore = false
    companion object {
        fun newInstance() = SearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_search, container, false)
        // val textView: TextView = root.findViewById(R.id.text_search)


//
//        setupViewModels()
//        setupObserver()
        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
//        islastPage= false
//        pageNumber = 1
//        itemsCount = 0
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)

        val args = arguments

        if (args != null) {
            //Todo this from Home Navigation tap we need to fix requesting
            islastPage = false
            colection = args!!.getInt(Constants.BUNDLE_COLLECTION_ID)
            topDeal = args!!.getInt(Constants.BUNDLE_TOPDEAL_ID)
            searchText = args!!.getString(Constants.BUNDLE_SearchText)
            collection_id = colection
            activityHostID = Constants.Activity_Host_Home
            if(BaseActivity.homeSearchResponse != null)  {
                /*     retrieveList(BaseActivity.homeSearchResponse!!,activityHostID)
                     shimmerFrameLayout.visibility = View.GONE
                     shimmerFrameLayout.stopShimmerAnimation()
                     recyclerview_search_product.visibility = View.VISIBLE*/
                if (productListSearView.isNullOrEmpty())
                    callNetwork(searchText,collection_id,topDeal)
                else{
                    shimmerFrameLayout.visibility = View.GONE
                    shimmerFrameLayout.stopShimmerAnimation()
                    recyclerview_search_product.visibility = View.VISIBLE
                    searchAdapter.replaceItems(productListSearView)
                    searchAdapter.notifyDataSetChanged()
                    text_items_count.visibility = View.VISIBLE
                    text_items_count.text = productListSearView.size.toString()
                    text_header_product_count.visibility = View.VISIBLE

                }
            }else {
                if (productListSearView.isNullOrEmpty())
                    callNetwork(searchText,collection_id,topDeal)
                else{
                    shimmerFrameLayout.visibility = View.GONE
                    shimmerFrameLayout.stopShimmerAnimation()
                    recyclerview_search_product.visibility = View.VISIBLE
                    searchAdapter.replaceItems(productListSearView)
                    searchAdapter.notifyDataSetChanged()
                    text_items_count.text = productListSearView.size.toString()
                    text_items_count.visibility = View.VISIBLE
                    text_header_product_count.visibility = View.VISIBLE
                }
            }
            setupSearch()

            swipe_refresh_search.setOnRefreshListener {
                swipe_refresh_search.isRefreshing = false
                loadWithRefresh = true
                callNetwork(searchText,collection_id,topDeal)

            }


            /*     try {
                     productList= args.getParcelableArrayList<Product>("products") as ArrayList<Product>
                     productList[0].name?.let { Log.e("Product", it)

                     }
                 }catch ( exception: Exception){

                 }*/


        } else {
            // This form Search Tap itself
            activityHostID = Constants.Activty_Host_Search
            if (BaseActivity.searchTapSearchResponse != null) {
              //  pageNumber += 1

                setupSearch()

                if (productListSearView.isNullOrEmpty())
                    callNetwork(searchText,collection_id,topDeal)
                else{
                    shimmerFrameLayout.visibility = View.GONE
                    shimmerFrameLayout.stopShimmerAnimation()
                    recyclerview_search_product.visibility = View.VISIBLE
                    searchAdapter.replaceItems(productListSearView)
                    searchAdapter.notifyDataSetChanged()
                    text_items_count.visibility = View.VISIBLE
                    text_items_count.text = productListSearView.size.toString()
                    text_header_product_count.visibility = View.VISIBLE
                }



//                retrieveList(BaseActivity.searchTapSearchResponse!!,activityHostID,true)
//                shimmerFrameLayout.visibility = View.GONE
//                shimmerFrameLayout.stopShimmerAnimation()
//                recyclerview_search_product.visibility = View.VISIBLE
                btn_sort_search.setOnClickListener {
                    if(!productListSearView.isNullOrEmpty()){

                        if (isAscending)
                            productListSearView.sortByDescending { it.price }
                        else
                            productListSearView.sortBy { it.price }

                        isAscending = !isAscending

                        searchAdapter.replaceItems(productListSearView)
                        searchAdapter.notifyDataSetChanged()
                    }
                }
                swipe_refresh_search.setOnRefreshListener {
                    loadWithRefresh = true
                    swipe_refresh_search.isRefreshing = false
                }
                return
            }
            if (pageNumber > 1)
                pageNumber -= 1
            callNetwork(searchText,collection_id,topDeal)

            setupSearch()
            swipe_refresh_search.setOnRefreshListener {
                swipe_refresh_search.isRefreshing = false
                loadWithRefresh = true
                if (pageNumber > 1)
                    pageNumber -= 1
                callNetwork(searchText,collection_id,topDeal)
            }

        }
        btn_sort_search.setOnClickListener {
            if(!productListSearView.isNullOrEmpty()){

                if (isAscending)
                    productListSearView.sortByDescending { it.price }
                else
                    productListSearView.sortBy { it.price }

                isAscending = !isAscending

                searchAdapter.replaceItems(productListSearView)
                searchAdapter.notifyDataSetChanged()
            }
        }

        // TODO: Use the ViewModel
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmerAnimation()

        setupUI()
        setupFilterBottomSheet()
        fragment_home_search.setOnClickListener {
            collapseSheet()
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
    private fun setupFilterBottomSheet(){
        bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(bottom_sheet_filters)
        view?.measuredHeight?.let { bottomSheetBehavior.setPeekHeight(it) }
        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        //  card_total.visibility = View.VISIBLE

                    }
                    BottomSheetBehavior.STATE_EXPANDED ->{
                        //   card_total.visibility = View.GONE
                    }
                    BottomSheetBehavior.STATE_COLLAPSED ->{

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        // card_total.visibility = View.GONE

                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                        //card_total.visibility = View.GONE

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        //   card_total.visibility = View.GONE

                    }
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
        btn_filter_search.setOnClickListener{

            expandCollapseSheet()

        }
    }
    private fun setupUI(){
        var filteredSelected = HashSet<SpecValue>()
        val layoutManager =
            LinearLayoutManager(activity, GridLayoutManager.VERTICAL, false)
        val  verticalLayoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.VERTICAL,false)
        recyclerview_search_product.layoutManager = layoutManager
        searchAdapter = ProductSearchAdapter(productList,this){product ->
            this.navigateToProductScreen(product)

        }

        recyclerview_search_product.addOnScrollListener( object: PaginationListener(layoutManager) {
            override fun loadMoreItems() {

                if (islastPage)
                    return
                scrollToMore = true
                callNetwork(searchText,collection_id,topDeal)
            }

            override val isLastPage: Boolean
                get() = islastPage
            override val isLoading: Boolean
                get() =isSearchLoading
        })
        recyclerview_search_product.adapter = searchAdapter




        val  bottomSheetLayoutManager = LinearLayoutManager(this.context,
            LinearLayoutManager.VERTICAL,false)
        recycler_sheet_filter.layoutManager = bottomSheetLayoutManager
        filterAdapter = FilterSpecificationAdapter(filters)

        recycler_sheet_filter.adapter = filterAdapter
        btn_apply_filters.setOnClickListener {
            appliedFilters = filterAdapter.filterValues
            collapseSheet()
            callFilterNetwork()
            //   Toasty.success(requireContext(),"Apply Clicked",Toasty.LENGTH_LONG)
        }
        text_filter_by.setOnClickListener{
            collapseSheet()

        }

    }


    private fun callFilterNetwork(){
        if( appliedFilters.isEmpty()){
            searchAdapter.replaceItems(productListSearView)
            searchAdapter.notifyDataSetChanged()
            itemsCount  =productListSearView.size
            text_items_count?.text = itemsCount.toString()
            return
        }
        val listOfIDS= appliedFilters.map {  spec ->
            spec.id
        } as ArrayList<Int>
        val applyWithRequestBody = ApplyFilterRequest(listOfIDS,productTextSearch,collectionID = collection_id,categoryID =topDeal
        )

        val callFilterNetwork = apiInterface.applySearchFilter(applyWithRequestBody)
        callFilterNetwork.enqueue(object : Callback<FilterSearchRepsonse?> {
            override fun onResponse(
                call: retrofit2.Call<FilterSearchRepsonse?>,
                response: Response<FilterSearchRepsonse?>
            ) {
                val erroBody = response.errorBody().toString()
                val header = response.headers()
                val repsonse: FilterSearchRepsonse? = response.body()
                val text: String? = repsonse?.message
                if (text != null && text == "") {
                    updateListWith(repsonse)
                } else if (text != null) {
                    //  Toasty.error(applicationContext, "$text page", Toast.LENGTH_LONG, true).show();
                }

            }

            override fun onFailure(
                call: retrofit2.Call<FilterSearchRepsonse?>,
                t: Throwable
            ) {
                call.cancel()
            }
        })
    }


    private fun callNetwork(searchText:String?, collection_id:Int?,topDeal:Int?){
        var collectionIDSend:Int? = null
        var topDealToSend :Int? = null
        if (collection_id != 0 && collection_id != null){// because getting data from bundle = 0
            collectionIDSend  = collection_id
            activityHostID = Constants.Activity_Host_Home
        } else if (topDeal != 0 && topDeal != null){
            activityHostID = Constants.Activity_Host_Home
            topDealToSend = topDeal
        }
        if (islastPage)
            return
        isSearchLoading = true
        var number = pageNumber
        if (!searchText.isNullOrEmpty()){
            number = 1
        }
        call3 = apiInterface.testSearchProducts(topDealToSend,collectionIDSend,searchText,number)

        call3!!.enqueue(object : Callback<SearchResponse?> {
            override fun onResponse(
                call: retrofit2.Call<SearchResponse?>,
                response: Response<SearchResponse?>
            ) {
                shimmerFrameLayout.stopShimmerAnimation()
                shimmerFrameLayout.visibility = View.GONE
                isSearchLoading = false
                recyclerview_search_product.visibility = View.VISIBLE
                val erroBody = response.errorBody().toString()
                val header = response.headers()
                val loginResponse: SearchResponse? = response.body()
                val text: String? = loginResponse?.message
                if (text != null && text == "") {
                    pageNumber++
                    val pagination = loginResponse.pagination
                    if (pagination != null) {
                        if( pagination.currentPage == pagination.totalNumberOfPages && pagination.nextPage == null)
                            islastPage = true
                    }else {
                        islastPage = true
                    }

                    if((!searchText.isNullOrEmpty())||loadWithRefresh)

                        retrieveList(loginResponse,activityHostID,true)
                    else
                        retrieveList(loginResponse,activityHostID)

                } else  {
                    // Toasty.warning(requireContext(), getString(R.string.no_search_items) ,Toast.LENGTH_LONG, true).show();
                }

            }

            override fun onFailure(
                call: retrofit2.Call<SearchResponse?>,
                t: Throwable
            ) {
                if (shimmerFrameLayout != null){
                    shimmerFrameLayout.stopShimmerAnimation()
                    shimmerFrameLayout.visibility = View.GONE
                    isSearchLoading = false
                    Toasty.warning(requireContext(),"Check Internet Connection").show()
                    recyclerview_search_product.visibility = View.VISIBLE
                    call.cancel()
                }
            }
        })



    }

    override fun onResume() {
        super.onResume()
        // shimmerFrameLayout.startShimmerAnimation()

    }

    private fun setupObserver(){
        searchViewModel
        searchViewModel.getSearch(SearchQueryRequest()).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        shimmerFrameLayout.stopShimmerAnimation()
                        shimmerFrameLayout.visibility = View.GONE

                        recyclerview_search_product.visibility = View.VISIBLE
                        //  progressBar.visibility = View.GONE
                        //resource.data?.let { searchData -> retrieveList(searchData) }
                    }
                    Status.ERROR -> {
                        recyclerview_search_product.visibility = View.VISIBLE
                        //  Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        shimmerFrameLayout.visibility = View.VISIBLE

                        ///   progressBar.visibility = View.VISIBLE
                        recyclerview_search_product.visibility = View.GONE

                    }
                }
            }
        })

    }

    private fun updateListWith(filterResponse:FilterSearchRepsonse){
        val filterProducts = filterResponse.data
        searchAdapter.replaceItems(filterProducts)
        searchAdapter.notifyDataSetChanged()
        text_items_count.text = filterProducts.size.toString()
        //  filterAdapter.notifyDataSetChanged()
    }

    private fun retrieveList(searchResponse:SearchResponse, fragmentTapHost:Int, willReplace:Boolean = false){
        loadWithRefresh = false
        val specs = searchResponse.data.specifications
        var data = searchResponse.data.products
//this for no items in store

        if( BaseActivity.homeSearchResponse == null || BaseActivity.searchTapSearchResponse == null){
            if (data.isNullOrEmpty())
                text_no_search_in_store.visibility = View.VISIBLE
        }

        val listOfProductInCart =  getCarts()
        val listOfProductInWishList = getWish()
        /*  data.forEach { product ->
              listOfProductInCart.forEach {productChecked ->
                  if (product.id == productChecked){
                      product.isCartSelected = true
                  }
              }
          }*/

        listOfProductInCart.forEach { cartID ->
            data.forEach {product ->
                if (product.id == cartID){
                    product.isCartSelected = true
                }
            }

        }


        listOfProductInWishList.forEach { wishID ->
            data.forEach{product ->
                if (product.id == wishID){
                    product.isWishSelected = true
                }
            }

        }
        suggestions = productListSearView.map { it.name }
        text_items_count.visibility = View.VISIBLE
        recyclerview_search_product.visibility = View.VISIBLE
        text_header_product_count.visibility = View.VISIBLE

        if (activityHostID == Constants.Activity_Host_Home){
            BaseActivity.homeSearchResponse = searchResponse
        } else if (activityHostID == Constants.Activty_Host_Search){
            BaseActivity.searchTapSearchResponse = searchResponse
        }
        if (!willReplace) {
            productListSearView.addAll(data)
            //searchAdapter.addItems(data)
            itemsCount  = productListSearView.size
            // searchAdapter.addItems(data)

            text_items_count?.text = itemsCount.toString()
        }else{

            itemsCount  =data.size
            productListSearView = data

            text_items_count?.text = itemsCount.toString()
            searchAdapter.replaceItems(data)

        }
        searchAdapter.notifyDataSetChanged()

        if (!specs.isNullOrEmpty()){
            if (scrollToMore){
                filterAdapter.addItems(specs)
                filterAdapter.notifyDataSetChanged()
            }else {
                specs?.let { filterAdapter.replaceItems(it) }
                filterAdapter.notifyDataSetChanged()
            }

        }
        scrollToMore = false


    }
    private fun collapseSheet(){
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottom_sheet_filters.visibility = View.GONE
    }
    private fun expandCollapseSheet() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottom_sheet_filters.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottom_sheet_filters.visibility = View.GONE

        }
    }

    private fun setupSearch(){
        //  val searchItem = menu.findItem(R.id.action_search)
        // val view: View = requireActivity().findViewById(R.id.search_searchvc)
        val searchView = requireActivity().findViewById(R.id.search_searchvc) as SearchView
        searchView.visibility = View.VISIBLE
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

        searchView.suggestionsAdapter = cursorAdapter
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(suggestions ==null)
                    return false
                if(!query.isNullOrEmpty()){
                    productTextSearch = query!!
                    callNetwork(query,collection_id,topDeal)

                }


//                suggestions.let {
//                    if(!suggestions!!.contains(query)){
//                        productTextSearch = query!!
//                        callNetwork(query,null,null)
//                    }
//                }

                hideKeyboard()
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor = MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                query?.let {
                    if (suggestions == null)
                        return  true
                    suggestions!!.forEachIndexed { index, suggestion ->
                        if (suggestion.contains(query, true))
                            cursor.addRow(arrayOf(index, suggestion))
                    }
                }
                cursorAdapter.changeCursor(cursor)

                if (query.isNullOrBlank()){
                    hideKeyboard()
                    searchAdapter.replaceItems(productListSearView)
                    searchAdapter.notifyDataSetChanged()

                }

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
                //    val suggestProducts = ArrayList<Product>()
                val suggestProducts = productListSearView.filter { it.name == selection } as ArrayList<Product>
                // suggestProducts.add(productListSearView[position])
                searchAdapter.replaceItems(suggestProducts)
                searchAdapter.notifyDataSetChanged()
                // Do something with selection
                return true
            }

        })



    }

    override fun onPause() {
        super.onPause()
        val searchView = requireActivity().findViewById(R.id.search_searchvc) as SearchView
        searchView.setQuery("",false)
        call3?.cancel()
    }

    fun SearchView.getQueryTextChangeStateFlow()


            : StateFlow<String> {

        val query = MutableStateFlow("")

        setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                query.value = newText

                return true
            }
        })

        return query

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
        val json = mPrefs.getString(Constants.sharedKey_Add_Cart, "")

        val type = object : TypeToken<Set<Product>>(){}.type
        var productsInCart: HashSet<Product>? = gson.fromJson(json, type)
        if (productsInCart != null){
            wishlistInt = productsInCart.map {
                it.id!!
            } as ArrayList<Int>
        }

        return wishlistInt
    }

    override fun onRecycleViewCartClicked(product: Product,checked: Boolean) {

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
        cartManger.updateCarts(allInCart!!)
    }

    override fun onRecycleViewLWishClicked(product: Product,checked:Boolean) {
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
                        return }
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
}