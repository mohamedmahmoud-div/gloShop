package com.treecode.GloShop.ui.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.Cateogry
import com.treecode.GloShop.ui.main.home.HomeFragment.Companion.collectionWidth
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

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
//        DataViewHolder(
//            LayoutInflater.from(parent.context)
//                .inflate(R.layout.item_collection_layout, parent, false)
//
//        )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View = inflater.inflate(R.layout.item_collection_layout, parent, false)

        // here we override the inflated view's height to be half the recyclerview size
        val layoutParams = itemView.layoutParams as MarginLayoutParams
//        layoutParams.height =
//            (parent.getWidth() / 2) - layoutParams.leftMargin - layoutParams.rightMargin;
   if(collectionWidth == 0) {
       layoutParams.width = (parent.getWidth() / 2) - layoutParams.leftMargin - layoutParams.rightMargin
       collectionWidth =  (parent.getWidth() / 2) - layoutParams.leftMargin - layoutParams.rightMargin

   }else
       layoutParams.width = collectionWidth
   // itemView.layoutParams = layoutParams
        return  DataViewHolder(itemView)
    }
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