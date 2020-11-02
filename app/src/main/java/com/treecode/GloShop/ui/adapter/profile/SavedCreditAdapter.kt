package com.treecode.GloShop.ui.adapter.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.profile.CreditCard
import kotlinx.android.synthetic.main.item_debit_card.view.*

class SavedCreditAdapter( private val savedCreditCards:HashSet<CreditCard>):RecyclerView.Adapter<SavedCreditAdapter.DataViewHolder>(){
class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(creditCard: CreditCard) {
itemView.text_card_number_add_payment.text = creditCard.carNumber
itemView.text_card_expire_add_payment.text = creditCard.expireDate
itemView.text_card_cvv_add_payment.text = creditCard.cvv
        itemView.text_card_name_add_payment.text = creditCard.ownerName
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SavedCreditAdapter.DataViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_debit_card, parent, false)
        )
    override fun getItemCount(): Int {
    return  savedCreditCards.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
val creditCard = savedCreditCards.elementAt(position)
        holder.bind(creditCard)
    }
    fun addCards(cards:HashSet<CreditCard>){
        savedCreditCards.addAll(cards)
    }
}