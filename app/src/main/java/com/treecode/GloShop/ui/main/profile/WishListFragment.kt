package com.treecode.GloShop.ui.main.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.ui.adapter.carts.CartItemChanged
import com.treecode.GloShop.ui.adapter.profile.MyWishAdapter
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.search.ProductDetailsFragment
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.Constants.Companion.prefrencKey
import com.treecode.GloShop.util.MyWishManger
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_wish_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WishListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WishListFragment : Fragment(),CartItemChanged {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var wishAdapter : MyWishAdapter
    private var products = HashSet<CartProduct>()
    private var updatedProducts = HashSet<CartProduct>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wish_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }
    private fun setupUI(){
        val  verticalLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recyclerview_myWish.layoutManager = verticalLayoutManager

        wishAdapter = MyWishAdapter(products,this)
        recyclerview_myWish.adapter = wishAdapter
        val wishManger = MyWishManger(requireContext())
        val wishList = wishManger.getWishList()
        if (wishList != null){
            if (!wishList.isEmpty()) {
                products = wishList
                updatedProducts = wishList
                wishAdapter.addItems(products)
                wishAdapter.notifyDataSetChanged()
                text_wish_empty.visibility = View.GONE
                return
            }
        }
        text_wish_empty.visibility = View.VISIBLE
        image_wish_error.visibility = View.VISIBLE
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WishListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WishListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onQuantityChanged(product: CartProduct) {

    }

    override fun onItemDelete(product: CartProduct) {
        if(updatedProducts.contains(product)){
            updatedProducts.remove(product)
            if (updatedProducts.size == 0)
                showNoItemsInCart()
        }
        wishAdapter.replaceItems(updatedProducts)
        wishAdapter.notifyDataSetChanged()
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
        fc?.replaceFragment(fragment)

    }
    fun showNoItemsInCart(){
        text_wish_empty.visibility = View.VISIBLE
        recyclerview_myWish.visibility = View.GONE

    }

    private fun updateCart(products: HashSet<CartProduct>){
        val gson = Gson()

        val mPrefs: SharedPreferences =
            requireContext().getSharedPreferences(prefrencKey, Context.MODE_PRIVATE)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val json = mPrefs.getString(Constants.sharedKey_Add_Wish, "")

        val type = object : TypeToken<Set<CartProduct>>(){}.type

        val jsonToSave = gson.toJson(products)
        prefsEditor.putString(Constants.sharedKey_Add_Wish,jsonToSave)
        prefsEditor.commit()
    }
}