package com.treecode.GloShop.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.util.CartsManger
import com.treecode.GloShop.util.MyWishManger
import kotlinx.android.synthetic.main.item_product_search.view.*


class ProductSearchAdapter  (private var products:ArrayList<Product>,private val rollBack:RecyclerViewCallback, private val listener: (Product) -> Unit):
    RecyclerView.Adapter<ProductSearchAdapter.DataViewHolder>(){
    private  var productFilterList = ArrayList<Product>()

    init {
        productFilterList = products
    }
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartIsSelected = false
        var wishIsSelected = false
        var pieceCount = 1

        fun bind(prodcut: Product) {



            itemView.text_product_name_search.text = prodcut.name
            itemView.text_product_category_search.text = prodcut.categoryName.name
            itemView.text_product_price_search.text = prodcut.price.toString()
            val offer = prodcut.offer
            if (offer!= null){
                itemView.text_search_product_after_discount_price.visibility = View.VISIBLE
                itemView.line_offer_change_search.visibility = View.VISIBLE
                itemView.text_search_product_after_discount_price.text = offer.afterPrice.toString()
                //   itemView.text_product_offer.visibility = View.VISIBLE
                // itemView.text_product_offer.text = offer.discount.toString()
            }
            itemView.text_product_count_search.text = "${prodcut.pieceCount}"
            if (!prodcut.hasSpecs){
                itemView.btn_cart_search.visibility = View.VISIBLE
                itemView.button_minus_search.visibility = View.VISIBLE
                itemView.button_plus_search.visibility = View.VISIBLE
                itemView.text_product_count_search.visibility = View.VISIBLE
            } else  {
                itemView.btn_cart_search.visibility = View.GONE
                itemView.button_minus_search.visibility = View.GONE
                itemView.button_plus_search.visibility = View.GONE
                itemView.text_product_count_search.visibility = View.GONE
            }
            if (prodcut.isCartSelected){
                itemView.btn_cart_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_uncart)

            }else{
                itemView.btn_cart_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_add_cart)
            }

            if (prodcut.isWishSelected){
                itemView.btn_add_wish_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_unwish)

            }else{
                itemView.btn_add_wish_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_wish)

            }
            Glide.with(itemView).load(prodcut.main_img).into(itemView.image_product_search);
            if (!prodcut.hasSpecs){
                itemView.btn_cart_search.visibility = View.VISIBLE
            } else  {
                itemView.btn_cart_search.visibility = View.GONE

            }

            val cartManger = CartsManger(itemView.context)
            var allInCart = cartManger.getAllProducts()
            if (!allInCart.isNullOrEmpty()) {
                val items = allInCart.filter { it.id == prodcut.id }
                if (!items.isNullOrEmpty()){
                    itemView.btn_cart_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_uncart)
                    cartIsSelected = true

                }else{
                    itemView.btn_cart_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_add_cart)
                    cartIsSelected = false

                }

            }

            val wishManger = MyWishManger(itemView.context)
            var allInWish = wishManger.getWishList()
            if (!allInWish.isNullOrEmpty()) {
                val items = allInWish.filter { it.id == prodcut.id }
                if (!items.isNullOrEmpty()){
                    itemView.btn_add_wish_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_unwish)
                    wishIsSelected = true

                }else{
                    itemView.btn_add_wish_search.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_wish)
                    wishIsSelected = false
                }

            }

        }

    }

    fun addItems(productsList:ArrayList<Product>){
        productFilterList.addAll(productsList)

    }
    fun replaceItems(productsList: ArrayList<Product>){
        productFilterList = productsList
        products = productsList

    }
    fun updateItems(productsList: ArrayList<Product>){
        productFilterList = productFilterList
        products = productFilterList
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ProductSearchAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_search, parent, false)
        )

    override fun getItemCount(): Int {
        return productFilterList.count()
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val product =  productFilterList[position]
        if (product.pieceCount == 0){
            product.pieceCount = 1
        }else {
            holder.pieceCount = product.pieceCount
        }
        holder.bind(product)
        holder.itemView.setOnClickListener {
            listener(product)
        }

        holder.itemView.button_plus_search.setOnClickListener {
            if(holder.pieceCount >= product.availability!!)
                return@setOnClickListener
            holder.pieceCount ++
            holder.itemView.text_product_count_search.text = holder.pieceCount.toString()
            product.pieceCount = holder.pieceCount

        }
        holder.itemView.button_minus_search.setOnClickListener {
            if (holder.pieceCount > 1) {
                holder.pieceCount--
                product.pieceCount = holder.pieceCount
                holder.itemView.text_product_count_search.text = holder.pieceCount.toString()

            }
        }
        holder.itemView.btn_cart_search.setOnClickListener{
            if(!holder.cartIsSelected){
                holder.cartIsSelected = !holder.cartIsSelected
                holder.itemView.btn_cart_search.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_uncart)
                this. rollBack.onRecycleViewCartClicked(product,true)

            } else {
                holder.cartIsSelected = !holder.cartIsSelected
                holder.itemView.btn_cart_search.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_add_cart)
                this. rollBack.onRecycleViewCartClicked(product,false)

            }

        }
        holder.itemView.btn_add_wish_search.setOnClickListener{
            if (!holder.wishIsSelected){
                holder.wishIsSelected = !holder.wishIsSelected
                holder.itemView.btn_add_wish_search.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_unwish)
                this. rollBack.onRecycleViewLWishClicked(product,true)

            }else{
                holder.wishIsSelected = !holder.wishIsSelected
                holder.itemView.btn_add_wish_search.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_wish)
                this. rollBack.onRecycleViewLWishClicked(product,false)
            }

        }

    }
}