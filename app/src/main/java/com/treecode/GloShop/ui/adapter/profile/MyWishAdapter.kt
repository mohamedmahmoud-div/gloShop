package com.treecode.GloShop.ui.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.CartProduct
import com.treecode.GloShop.ui.adapter.carts.CartItemChanged
import kotlinx.android.synthetic.main.item_my_wishlist.view.*

class MyWishAdapter
    (private var products: HashSet<CartProduct>,
     private val cartDataChanged: CartItemChanged

):RecyclerView.Adapter<MyWishAdapter.DataViewHolder>()
{

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(prodcut: CartProduct) {
            itemView.text_my_wish_category_name.text = prodcut.categoryName
            itemView.text_my_wish_product_name.text = prodcut.name
            itemView.text_my_wish_price.text = prodcut.price.toString()
            Glide.with(itemView).load(prodcut.main_img).into(itemView.image_my_wish);

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=

        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_wishlist, parent, false)

        )

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val product = products.elementAt(position)
        holder.itemView.btn_unwish_profile.setOnClickListener {
            cartDataChanged.onItemDelete(product)
        }
        holder.itemView.setOnClickListener {
            cartDataChanged.onItemClickToView(product)
        }

        holder.bind(product)
    }
    fun addItems(productsList:HashSet<CartProduct>){
        products.addAll(productsList)
    }
    fun replaceItems(productsList:HashSet<CartProduct>){
        products = productsList
    }
}

