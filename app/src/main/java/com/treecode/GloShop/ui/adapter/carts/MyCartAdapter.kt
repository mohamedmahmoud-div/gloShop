package com.treecode.GloShop.ui.adapter.carts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.CartProduct
import kotlinx.android.synthetic.main.item_my_cart.view.*

class MyCartAdapter ( val products: HashSet<CartProduct>, private val cartDataChanged:CartItemChanged
) : RecyclerView.Adapter<MyCartAdapter.DataViewHolder>(){

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plusBtn = itemView.button_plus_my_cart
        val minusBtn = itemView.button_minus_my_cart
        val textPieceCount = itemView.text_piece_count
        val  texttotalproductCountPrice = itemView.total_product_count
        val deletButton = itemView.btn_delete
        var pieceCount = 1

        @SuppressLint("SetTextI18n")
        fun bind(prodcut: CartProduct) {

            itemView.text_my_cart_category_name.text = prodcut.categoryName
            itemView.text_my_cart_product_name.text = prodcut.name
            if (prodcut.totalAvailbilty == 0){
                itemView.text_store_quantity.visibility = View.VISIBLE
                itemView.text_store_quantity.text = itemView.context.getString(R.string.out_of_stock)
            } else if (prodcut.totalAvailbilty!! < prodcut.quantity){
                itemView.text_store_quantity.visibility = View.VISIBLE
                itemView.text_store_quantity.text =  "${prodcut.totalAvailbilty}" +itemView.context.getString(R.string.remaining_in_stock)
            }else{
                itemView.text_store_quantity.visibility = View.GONE

            }
            if (prodcut.afterPrice == null)
            itemView.text_my_cart_price.text = prodcut.price.toString()
            else
                itemView.text_my_cart_price.text = prodcut.afterPrice.toString()
            textPieceCount.text = prodcut.quantity.toString()
            if(prodcut.afterPrice == null)
            texttotalproductCountPrice.text = (prodcut.price * prodcut.quantity) .toString()
            else
                texttotalproductCountPrice.text = (prodcut.afterPrice * prodcut.quantity) .toString()

            Glide.with(itemView).load(prodcut.main_img).into(itemView.image_my_cart);

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_cart, parent, false)

        )


    override fun getItemCount(): Int {
        return  products.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val prodcut = products.elementAt(position)
        if (prodcut.quantity == 0){
            prodcut.quantity = 1
        }else {
            holder.pieceCount = prodcut.quantity
        }


        holder.bind(prodcut)

        holder.itemView.setOnClickListener {
            cartDataChanged.onItemClickToView(prodcut)
        }


        holder.plusBtn.setOnClickListener {
            if(prodcut.totalAvailbilty != null){
                if(holder.pieceCount >= prodcut.totalAvailbilty!!)
                    return@setOnClickListener
            }

           holder. pieceCount ++
            holder.textPieceCount.text = holder.pieceCount.toString()
            if (prodcut.afterPrice == null)
            holder.texttotalproductCountPrice.text = (prodcut.price * holder.pieceCount).toString()
else
                holder.texttotalproductCountPrice.text = (prodcut.afterPrice * holder.pieceCount).toString()

            prodcut.quantity = holder.pieceCount
            cartDataChanged.onQuantityChanged(prodcut)
        }
        holder.minusBtn.setOnClickListener {
            if (holder.pieceCount > 1){
                holder.pieceCount--
            if (holder.pieceCount <prodcut.totalAvailbilty!!){
                holder.itemView.text_store_quantity.visibility = View.GONE

            }
                holder.texttotalproductCountPrice.text = (prodcut.price * holder.pieceCount).toString()
            holder.textPieceCount.text = holder.pieceCount.toString()
            prodcut.quantity = holder.pieceCount
                cartDataChanged.onQuantityChanged(prodcut)
        }


        }
        holder.deletButton.setOnClickListener{
            removeAt(position,prodcut)
            cartDataChanged.onItemDelete(prodcut)
        }
    }
    fun removeAt(position: Int,product: CartProduct) {
        products.remove(product)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, products.size)
    }
fun addItems(productsList:HashSet<CartProduct>){
    products.addAll(productsList)
}
}
interface  CartItemChanged{
    fun onQuantityChanged(product: CartProduct)
    fun  onItemDelete(product: CartProduct)
    fun onItemClickToView(product: CartProduct)
}