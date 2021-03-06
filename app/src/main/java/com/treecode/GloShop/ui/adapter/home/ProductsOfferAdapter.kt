package com.treecode.GloShop.ui.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.Offer
import com.treecode.GloShop.ui.adapter.RecyclerViewCallback
import com.treecode.GloShop.util.CartsManger
import com.treecode.GloShop.util.MyWishManger
import kotlinx.android.synthetic.main.item_product_layout.view.*

class ProductsOfferAdapter(
    private var productsOffer:ArrayList<Offer>, private val rollBack: RecyclerViewCallback, private val listener: (Offer) -> Unit

):RecyclerView.Adapter<ProductsOfferAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartIsSelected = false
        var wishIsSelected = false
        fun bind(offer: Offer) {
            val prodcut = offer.product
            itemView.text_arrival_product_after_discount_price.visibility = View.VISIBLE
            itemView.line_offer_change.visibility = View.VISIBLE
            val productOffer = prodcut.offer
            if (productOffer!= null)
                if (productOffer.afterPrice != prodcut.price){
                    itemView.text_arrival_product_after_discount_price.text = productOffer.afterPrice.toString()
                }else {
                    itemView.text_arrival_product_after_discount_price.text = productOffer.type
                }

            if (prodcut.hasSpecs){
                itemView.button_arrival_cart.visibility = View.GONE
            } else  {
                itemView.button_arrival_cart.visibility = View.VISIBLE

            }
            itemView.text_arrival_product_name.text = prodcut.name
            itemView.text_arrival_category_name.text = prodcut.name
            itemView.text_arrival_product_price.text = prodcut.price.toString()
            itemView.text_product_offer.text = offer.discount.toString()
            if (prodcut.stars != null) {
                itemView.layout_item_product_rate.visibility = View.VISIBLE
                itemView.text_rate_percent.text = prodcut.stars
            }else {
                itemView.layout_item_product_rate.visibility = View.GONE
            }
            itemView.text_product_offer.visibility = View.VISIBLE
            itemView.text_percentage.visibility = View.VISIBLE
            if (prodcut.isCartSelected){
                itemView.button_arrival_cart.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_uncart)

            }else{
                itemView.button_arrival_cart.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_add_cart)
            }

            if (prodcut.isWishSelected){
                itemView.button_arrival_wishlist.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_unwish)

            }else{
                itemView.button_arrival_wishlist.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_wish)

            }
            Glide.with(itemView).load(prodcut.main_img).into(itemView.image_arrival_product);
            val cartManger = CartsManger(itemView.context)
            var allInCart = cartManger.getAllProducts()
            if (!allInCart.isNullOrEmpty()) {
                val items = allInCart.filter { it.id == prodcut.id }
                if (!items.isNullOrEmpty()){
                    itemView.button_arrival_cart.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_uncart)
                    cartIsSelected = true
                }else{
                    itemView.button_arrival_cart.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_add_cart)
                    cartIsSelected = false

                }

            }
            val wishManger = MyWishManger(itemView.context)
            var allInWish = wishManger.getWishList()
            if (!allInWish.isNullOrEmpty()) {
                val items = allInWish.filter { it.id == prodcut.id }
                if (!items.isNullOrEmpty()){
                    itemView.button_arrival_wishlist.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_unwish)
                    wishIsSelected = true
                }else{
                    itemView.button_arrival_wishlist.background = ContextCompat.getDrawable(itemView.context, R.drawable.ic_wish)
                    wishIsSelected = false
                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductsOfferAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_layout, parent, false)
        )

    override fun getItemCount(): Int {
        return productsOffer.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val offer = productsOffer[position]
        val product = offer.product
        holder.bind(offer)
        holder.itemView.setOnClickListener {
            listener(offer)
        }
        holder.itemView.button_arrival_cart.setOnClickListener{
            if(!holder.cartIsSelected){
                holder.cartIsSelected = !holder.cartIsSelected
                holder.itemView.button_arrival_cart.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_uncart)
                this. rollBack.onRecycleViewCartClicked(product,true)

            } else {
                holder.cartIsSelected = !holder.cartIsSelected
                holder.itemView.button_arrival_cart.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_add_cart)
                this. rollBack.onRecycleViewCartClicked(product,false)


            }

            //TODO Add To SharedPreference
        }
        holder.itemView.button_arrival_wishlist.setOnClickListener{
            if(!holder.wishIsSelected){
                holder.wishIsSelected = !holder.wishIsSelected
                holder.itemView.button_arrival_wishlist.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_unwish)
                this.rollBack.onRecycleViewLWishClicked(product,true)

            } else {
                holder.wishIsSelected = !holder.wishIsSelected
                holder.itemView.button_arrival_wishlist.background = ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_wish)
                this.rollBack.onRecycleViewLWishClicked(product,false)


            }

        }
    }
    fun addNewItems(offers:ArrayList<Offer>){
        // this.productsOffer.addAll(offers)
        this.productsOffer = offers
    }

    fun replaceArrivals(offers: java.util.ArrayList<Offer>) {
        this.productsOffer = offers
    }
}