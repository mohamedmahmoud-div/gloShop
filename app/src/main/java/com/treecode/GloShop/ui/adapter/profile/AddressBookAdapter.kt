package com.treecode.GloShop.ui.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.profile.AddressBook
import kotlinx.android.synthetic.main.item_address.view.*

class AddressBookAdapter (
    private var listOfAddress:HashSet<AddressBook>,
    private val onEditClick:(addressBook:AddressBook)->Unit,
    private val onDeleteClick:(addreesBookID:AddressBook,postion:Int)->Unit ,
    private val onSetDefaultClick:(addressBook:AddressBook)->Unit
):RecyclerView.Adapter<AddressBookAdapter.DataViewHolder>(){
class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(userAddress: AddressBook) {
        itemView.text_location_checkout.text =  userAddress.streetAddress
        itemView.text_city_checkout.text = userAddress.city.name
        itemView.text_country_checkout.text = userAddress.country.name

    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        AddressBookAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        )
    override fun getItemCount(): Int {
        return listOfAddress.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val address = listOfAddress.elementAt(position)
        holder.itemView.btn_edit_address.setOnClickListener {
            onEditClick(address)
        }
        holder.itemView.btn_delete_address.setOnClickListener {
            onDeleteClick(address,position)
        }
        holder.itemView.btn_set_default_address.setOnClickListener {
            onSetDefaultClick(address)
        }
        holder.bind(address)
    }
    fun addItems(newItems:HashSet<AddressBook>){
        listOfAddress.addAll(newItems)
    }
    fun replaceItems(newItems: HashSet<AddressBook>){
        listOfAddress = newItems
    }
    fun remove(address:AddressBook){
        listOfAddress.remove(address)
    }
}