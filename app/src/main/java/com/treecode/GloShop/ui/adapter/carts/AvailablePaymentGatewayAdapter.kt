package com.treecode.GloShop.ui.adapter.carts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.checkout.AvailabelPaymentGateway
import kotlinx.android.synthetic.main.item_payment_gateway.view.*

class AvailablePaymentGatewayAdapter(private val gateways:ArrayList<AvailabelPaymentGateway>,private val selectedListener:(gatwayID:Int)->Unit):RecyclerView.Adapter<AvailablePaymentGatewayAdapter.DataViewHolder>() {
    var selectedPostion = -1
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(gateway: AvailabelPaymentGateway) {
            Glide.with(itemView).load(gateway.logo).centerCrop().into(itemView.image_payment_gateway_logo);
            itemView.text_payment_gateway_name.text = gateway.name
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
      DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_payment_gateway, parent, false)
        )

    override fun getItemCount(): Int {
return gateways.count()
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
    val gateway = gateways[position]
        holder.bind(gateway)
        if (selectedPostion == position){
            holder.itemView.btn_payment_selection.visibility =View.VISIBLE
        }else {
            holder.itemView.btn_payment_selection.visibility = View.INVISIBLE

        }
        holder.itemView.setOnClickListener{

                val periousSelectd = selectedPostion
            selectedPostion = position
            notifyItemChanged(periousSelectd)
         //   holder.itemView.radio_payment_selection.isChecked  = true
           notifyItemChanged(selectedPostion)
            selectedListener(gateway.id)
        }
    }
}