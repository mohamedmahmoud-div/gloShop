package com.treecode.GloShop.ui.adapter.carts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.CartProduct
import kotlinx.android.synthetic.main.item_product_checkout.view.*

class OrderCheckoutAdapter (private val cartProducts: HashSet<CartProduct>):RecyclerView.Adapter<OrderCheckoutAdapter.DataViewHolder>(){


class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(product: CartProduct) {
        itemView.text_product_name_checkout.text = product.name
        itemView.text_product_category_checkout.text = product.categoryName
        if (product.afterPrice == null)
        itemView.text_product_price_checkout.text = (product.price * product.quantity).toString()
        else
            itemView.text_product_price_checkout.text = (product.afterPrice * product.quantity).toString()

        Glide.with(itemView).load(product.main_img).into(itemView.image_product_checkout);

    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        OrderCheckoutAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_checkout, parent, false)

        )

    override fun getItemCount(): Int {
        return cartProducts.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val product = cartProducts.elementAt(position)
        holder.bind(product)
    }
    fun addItems(productsList:HashSet<CartProduct>){
        cartProducts.addAll(productsList)
    }

}