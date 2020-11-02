package com.treecode.GloShop.ui.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.profile.OrderStatus
import kotlinx.android.synthetic.main.item_filter_order_status.view.*

class FilterStatusAdapter (
    private val allStatus:ArrayList<OrderStatus>
,val callBack:(OrderStatus)->Unit
):RecyclerView.Adapter<FilterStatusAdapter.DataViewHolder>(){
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(orderStatus: OrderStatus) {
            itemView.text_filter_status.text = orderStatus.value
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int)=
        FilterStatusAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter_order_status, parent, false)
        )

    override fun getItemCount(): Int {
        return  allStatus.size
    }

    override fun onBindViewHolder(holder: FilterStatusAdapter.DataViewHolder, position: Int) {
        val orderStatus = allStatus[position]
        holder.itemView.text_filter_status.setOnClickListener{
            callBack(orderStatus)
        }
        holder.bind(orderStatus)
    }
}