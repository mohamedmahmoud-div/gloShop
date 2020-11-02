package com.treecode.GloShop.ui.adapter.productspec

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.search.SpecValue
import com.treecode.GloShop.data.model.search.Specification
import kotlinx.android.synthetic.main.item_prodcut_deatils_spec.view.*
import java.lang.Exception

class ProductsAllSpecAdapter(
    private var allSpecs:ArrayList<Specification>,
    private val rollBack:AllSpecsChangeListener
):RecyclerView.Adapter<ProductsAllSpecAdapter.DataViewHolder>(){
    private val viewPool = RecyclerView.RecycledViewPool()
    var selectedValues = HashSet<Int>()
var colorValuse = ArrayList<SpecValue>()
    var colorSelectedID:Int? = null
  lateinit var  specColorAdapter:ProductColorSpecAdapter
  lateinit var  specTextAdapter:ProductTextSpecAdapter
class DataViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val colorsRecyclerView = itemView.recycler_view_product_spec_color
    val textRecyclerView = itemView.recycler_view_spec_text

    fun  bind(specValue: Specification){
itemView.text_product_spec_header.text = specValue.name
    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        ProductsAllSpecAdapter.DataViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_prodcut_deatils_spec, parent, false)

    )

    override fun getItemCount(): Int {
        return  allSpecs.size
    }
 fun addSpecs(allSpecs: ArrayList<Specification>){
     this.allSpecs.addAll(allSpecs)
 }
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val spec = allSpecs[position]
        holder.bind(spec)
        if (spec.isColor) {
            val colorsValues = spec.values
            specColorAdapter = ProductColorSpecAdapter(colorsValues) {color ->
                this.colorSelectedID = color.id
                try {
                    rollBack.onChange(spec,color)

                }catch (e:Exception){

                }
            }
            holder.colorsRecyclerView.visibility = View.VISIBLE
            holder.textRecyclerView.visibility = View.VISIBLE
            holder.colorsRecyclerView.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,false)
                adapter = specColorAdapter
                setRecycledViewPool(viewPool)

            }
        }else{
            holder.colorsRecyclerView.visibility = View.GONE
            val  textValues = spec.values
            specTextAdapter = ProductTextSpecAdapter(textValues){specValue,isSelected ->
            // todo send it Product Deatils fragment , then update Prodcuts AllSpec with availabity using filter
                //todo : using filter(to filter all specs) and notfiy data changed
                rollBack.onChange(spec,specValue)
            }
            holder.textRecyclerView.visibility = View.VISIBLE
            holder.colorsRecyclerView.visibility = View.GONE
            holder.textRecyclerView.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context,
                    LinearLayoutManager.HORIZONTAL,false)
                adapter = specTextAdapter
                setRecycledViewPool(viewPool)
            }

        }
    }
    fun  addSubSpec(spec:Specification){
        allSpecs.add(spec)
    /*    if (spec.isColor){
            val colors = spec.values
            specColorAdapter.addColors(colors)
            specColorAdapter.notifyDataSetChanged()
        }else {

        }*/

    }
}
interface AllSpecsChangeListener{
    fun onChange(spec:Specification,specValue: SpecValue)
}