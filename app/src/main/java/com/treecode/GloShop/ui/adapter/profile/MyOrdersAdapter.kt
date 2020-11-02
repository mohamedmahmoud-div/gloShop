package com.treecode.GloShop.ui.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.profile.Order
import com.treecode.GloShop.util.Constants.Companion.DELIVERED
import com.treecode.GloShop.util.Constants.Companion.ON_DELIVERY
import com.treecode.GloShop.util.Constants.Companion.ORDERED
import com.treecode.GloShop.util.Constants.Companion.SHIPPED
import kotlinx.android.synthetic.main.item_my_order.view.*

class MyOrdersAdapter (
    private var myOrders:ArrayList<Order>, private val orderClickListener:(orderId:Int)->Unit
):RecyclerView.Adapter<MyOrdersAdapter.DataViewHolder>(){
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(order: Order) {
            itemView.text_order_id.text = order.orderNumber
                val orderStatus = order.orderStatus
            itemView.order_address.text = order.shippingAddress
            val paymentMethod = order.paymentMethod
            itemView.my_order_payment.text = paymentMethod.value
            itemView.my_order_status.text = orderStatus.value
            if (orderStatus.id == DELIVERED) {
                itemView.order_date.text = order.deliveredDate // TODO(nee to update it deliver date
                val totalCost = order.productsCost + order.shippingFees
                    itemView.my_order_ship_fees.visibility = View.GONE
                itemView.text_order_sheep_fees_header.visibility = View.GONE
                itemView.my_order_total.text = totalCost
                itemView.my_order_status.setTextColor(ContextCompat.getColor(itemView.context, R.color.gree))
            }else{
                itemView.my_order_ship_fees.visibility = View.VISIBLE
                itemView.text_order_sheep_fees_header.visibility = View.VISIBLE
                itemView.my_order_total.text = order.productsCost
                itemView.my_order_ship_fees.text = order.shippingFees
                when(orderStatus.id) {
                    SHIPPED->  itemView.my_order_status.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
                    ON_DELIVERY ->  itemView.my_order_status.setTextColor(ContextCompat.getColor(itemView.context, R.color.blue))
                    ORDERED-> itemView.my_order_status.setTextColor(ContextCompat.getColor(itemView.context, R.color.gree))
                    4 -> println("Number too high, but acceptable")
                    else -> println("Number too high")
                }
            }
            itemView.my_order_status.text = orderStatus.value

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        MyOrdersAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_my_order, parent, false)
        )

    override fun getItemCount(): Int {
        return  myOrders.count()

    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val order = myOrders[position]
        holder.bind(order)
        holder.itemView.setOnClickListener {
            orderClickListener(order.id)
        }
    }
    fun addOrders(orders:ArrayList<Order>){
        myOrders.addAll(orders)
    }
    fun replace(orders:ArrayList<Order>){
        myOrders = orders
        //myOrders.addAll(orders)
    }
}
