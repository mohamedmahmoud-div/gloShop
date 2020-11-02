package com.treecode.GloShop.ui.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.Cateogry
import kotlinx.android.synthetic.main.item_collection_layout.view.*

class CateogiesAdapter(
    private var categorires :ArrayList<Cateogry>,private val clickListner: (Int) -> Unit): RecyclerView.Adapter<CateogiesAdapter.DataViewHolder>(){
    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun  bind(category: Cateogry){
            itemView.text_collection.text = category.name
            //
            Glide.with(itemView).load(category.image).centerCrop().into(itemView.imageView_collection)
            //Glide.with(itemView).load(category.image).into(itemView.imageView_collection)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_collection_layout, parent, false)

        )

    override fun getItemCount(): Int {
return categorires.count()
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val category = categorires[position]
        holder.bind(categorires[position])
        holder.itemView.setOnClickListener {
            clickListner(category.id)
        }
    }
    fun addCategoryies(categories:ArrayList<Cateogry>){
        this.categorires = categories
     //   categorires.addAll(categories)
    }
}