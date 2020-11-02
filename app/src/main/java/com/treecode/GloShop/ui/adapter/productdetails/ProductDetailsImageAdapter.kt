package com.treecode.GloShop.ui.adapter.productdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import kotlinx.android.synthetic.main.item_product_details_image.view.*

class ProductDetailsImageAdapter (private val productListOfImages:ArrayList<String>) : RecyclerView.Adapter<ProductDetailsImageAdapter.DataViewHolder>() {
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(image: String) {
            //
            Glide.with(itemView).load(image).into(itemView.image_product_details);

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  DataViewHolder =
    ProductDetailsImageAdapter.DataViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_product_details_image, parent, false)

    )


    override fun getItemCount(): Int {
return  productListOfImages.count()
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
holder.bind(productListOfImages[position])
    }
    fun addImages(images:ArrayList<String>){
        productListOfImages.addAll(images)
    }
    fun addSingleImage(image:String){
        productListOfImages.add(image)
    }

}