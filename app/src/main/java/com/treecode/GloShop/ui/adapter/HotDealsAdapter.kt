package com.treecode.GloShop.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.HotDeal
import kotlinx.android.synthetic.main.item_deal_layout.view.*

class HotDealsAdapter(
    private var deals:ArrayList<HotDeal>, private val dealClickLisner:(Int) -> Unit
): RecyclerView.Adapter<HotDealsAdapter.DataViewHolder>(){
    class DataViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun  bind(hotDeal: HotDeal){
            itemView.text_category_name.text = hotDeal.dealName
            itemView.text_offer_description.text = hotDeal.offer.description
            itemView.text_offer_type.text = hotDeal.offer.type.name
            itemView.btn_hot_deal_offer.text = hotDeal.offer.discount.toString() + "%"
         Glide.with(itemView).load(hotDeal.offer.image).into(itemView.imageView_deal);

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =  DataViewHolder (
          LayoutInflater.from(parent.context).inflate(R.layout.item_deal_layout,parent,false)

    )



    override fun getItemCount(): Int {
return  deals.count()
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val deal = deals[position]
holder.bind(deals[position])
        holder.itemView.setOnClickListener {
            dealClickLisner(deal.dealID)
        }
    }
    fun addDeals(dealList:ArrayList<HotDeal>){
        deals = dealList
    }
}