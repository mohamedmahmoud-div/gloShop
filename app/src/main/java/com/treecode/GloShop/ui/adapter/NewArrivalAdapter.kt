package com.treecode.GloShop.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.Product
import kotlinx.android.synthetic.main.item_product_layout.view.*

class NewArrivalAdapter(
    private var newArrivals:ArrayList<Product>, private val rollBack:RecyclerViewCallback, private val listener: (Product) -> Unit):
    RecyclerView.Adapter<NewArrivalAdapter.DataViewHolder>(){
    var recyclerViewCallback: RecyclerViewCallback? = null

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cartIsSelected = false
        var wishIsSelected = false
        fun bind(prodcut: Product) {
            if (!prodcut.hasSpecs){
                itemView.button_arrival_cart.visibility = View.VISIBLE
            } else  {
                itemView.button_arrival_cart.visibility = View.GONE

            }
            val offer = prodcut.offer
            if (prodcut.stars == null)
                itemView.text_rate_percent.text = "0"
            else
                itemView.text_rate_percent.text = prodcut.stars!!

            if (offer!= null){
                itemView.text_arrival_product_after_discount_price.visibility = View.VISIBLE
                itemView.line_offer_change.visibility = View.VISIBLE
                itemView.text_arrival_product_after_discount_price.text = offer.afterPrice.toString()
                itemView.text_product_offer.visibility = View.VISIBLE
                itemView.text_product_offer.text = offer.discount.toString()
            }

            itemView.text_arrival_product_name.text = prodcut.name
            itemView.text_arrival_category_name.text = prodcut.name
            itemView.text_arrival_product_price.text = prodcut.price.toString()

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

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= DataViewHolder (
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_layout,parent,false)
    )


    override fun getItemCount(): Int {
return  newArrivals.count()
    }

        override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
            val product =  newArrivals[position]
            holder.bind(product)

            holder.itemView.setOnClickListener {
                listener(product)
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
    fun setOnCallbackListener(recyclerViewCallback: RecyclerViewCallback) {
        this.recyclerViewCallback = recyclerViewCallback
    }

    fun addNewItems(arrivals:ArrayList<Product>){
        newArrivals = arrivals
    }

    fun replaceArrivals(arrivalsList: java.util.ArrayList<Product>) {
        newArrivals = arrivalsList
    }
}

interface RecyclerViewCallback {
    fun onRecycleViewCartClicked(product: Product, checked:Boolean)
    fun onRecycleViewLWishClicked(proudct: Product, checked: Boolean)
}