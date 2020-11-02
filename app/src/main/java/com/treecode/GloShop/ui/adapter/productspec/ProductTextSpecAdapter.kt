package com.treecode.GloShop.ui.adapter.productspec

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.search.SpecValue
import kotlinx.android.synthetic.main.item_product_text_spec.view.*
import java.lang.Exception

class ProductTextSpecAdapter (
    private val textSpecs: ArrayList<SpecValue> = ArrayList<SpecValue>(),
    val callBack:(specValue:SpecValue,isSelected:Boolean)->Unit
): RecyclerView.Adapter<ProductTextSpecAdapter.DataViewHolder>(){
    var selectedPostion = 0

class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var isSelected  =false

    fun  bind(specValue: SpecValue){
      itemView.btn_spec_text.text = specValue.value

        // itemView.btn_spec_color.setBackgroundColor()

    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductTextSpecAdapter.DataViewHolder =
        ProductTextSpecAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_text_spec, parent, false)

        )

    override fun getItemCount(): Int {
        return  textSpecs.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val spec = textSpecs[position]

        holder.bind(spec)

        if (selectedPostion == position) {
            holder.itemView.btn_spec_text.setTextColor(Color.parseColor("#E7A646"))

        }else{
            holder.itemView.btn_spec_text.setTextColor(Color.parseColor("#000000"))

        }
        holder.itemView.btn_spec_text.setOnClickListener {

            var periousSelectd = selectedPostion
                    if (selectedPostion == -1)
                        periousSelectd = 0
            selectedPostion = position
            holder.isSelected = !holder.isSelected
            try {
                callBack(spec,holder.isSelected)

            }catch (e:Exception){

            }
            notifyItemChanged(periousSelectd)
            notifyItemChanged(selectedPostion)
            // todo send callback th id oF specvalueID

        }
    }
}