package com.treecode.GloShop.ui.adapter.productspec

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.search.SpecValue
import kotlinx.android.synthetic.main.item_product_color_spec.view.*


class ProductColorSpecAdapter (
    private val specs: ArrayList<SpecValue> = ArrayList<SpecValue>()
,private val clickListner: (SpecValue) -> Unit
):RecyclerView.Adapter<ProductColorSpecAdapter.DataViewHolder>(){

    class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun  bind(specValue: SpecValue){
            val unwrappedDrawable =
                AppCompatResources.getDrawable(itemView.context, R.drawable.button_circle)
            val wrappedDrawable =
                DrawableCompat.wrap(unwrappedDrawable!!)
            DrawableCompat.setTint(wrappedDrawable, Color.parseColor(specValue.value))
            itemView.btn_spec_color.background = wrappedDrawable
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductColorSpecAdapter.DataViewHolder =
        ProductColorSpecAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_color_spec, parent, false)

        )


    override fun getItemCount(): Int {
return specs.size

    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
    val spec = specs[position]
        holder.bind(spec)
        holder.itemView.btn_spec_color.setOnClickListener{

            clickListner(spec)
        }
    }
    fun addColors(specs:ArrayList<SpecValue>){
        this.specs.addAll(specs)
    }
}

