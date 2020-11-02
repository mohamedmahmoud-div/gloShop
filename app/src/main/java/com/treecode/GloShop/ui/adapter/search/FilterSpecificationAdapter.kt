package com.treecode.GloShop.ui.adapter.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.search.SpecValue
import com.treecode.GloShop.data.model.search.Specification
import kotlinx.android.synthetic.main.item_filter_specification.view.*

class FilterSpecificationAdapter(private var filters: ArrayList<Specification>) :RecyclerView.Adapter<FilterSpecificationAdapter.DataViewHolder>(), ParentRecyclerCallBack{
    private lateinit var filterValuesAdapter:FilterValuesAdapter
    var filterValues = HashSet<SpecValue>()
    private val viewPool = RecyclerView.RecycledViewPool()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
val recyclerView = itemView.recycler_filter_values
    fun bind(filter: Specification) {
itemView.text_filter_type.text = filter.name

    }
}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        FilterSpecificationAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter_specification, parent, false)
        )

    override fun getItemCount(): Int {
    return  filters.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val filter = filters[position]
        filterValuesAdapter    = FilterValuesAdapter(filter.values,this)
        holder.recyclerView.apply {
         layoutManager = LinearLayoutManager(holder.recyclerView.context,
             LinearLayoutManager.VERTICAL,false)
            adapter = FilterValuesAdapter(filter.values,this@FilterSpecificationAdapter)
            setRecycledViewPool(viewPool)

        }
        holder.bind(filter)

    }
    fun addItems(filterList:ArrayList<Specification>){

            var newFilterPostion = 0
            var oldFilterPostion =0
            while (newFilterPostion < filterList.size) {
            val newFilter = filterList[newFilterPostion]
                while (oldFilterPostion < filters.size){
                    val oldFilter = filters[oldFilterPostion]
                    if (oldFilter.id == newFilter.id) {
                        this.compareSpecs(oldFilter,newFilter)
                        filters.remove(oldFilter)
                        oldFilterPostion++
                    }
                }
                newFilterPostion++
            }
//
//      /*  filterList.forEach { newFilter ->
//            filters.forEach{oldFilter ->
//                if (oldFilter.id == newFilter.id)
//                    filters.remove(oldFilter)
//            }
//        }*/
        filters = filterList
    }
    private fun compareSpecs(oldSpec:Specification,newSpec:Specification){
        var newFilterPostion = 0
        var oldFilterPostion =0
        val oldSpecValues = oldSpec.values
        val newSpecValues = newSpec.values
var sameValues = ArrayList<SpecValue>()

        while (newFilterPostion < newSpecValues.size) {
            val newFilter = newSpecValues[newFilterPostion]

            while (oldFilterPostion < oldSpecValues.size){
                val oldFilter = oldSpecValues[oldFilterPostion]
                if (oldFilter.id == newFilter.id) {
                    sameValues.add(oldFilter)
                }
                oldFilterPostion++

            }
            oldFilterPostion = 0
            newFilterPostion++
        }
//
        oldSpecValues.removeAll(sameValues)
        newSpecValues.addAll(oldSpecValues)
    }
    fun replaceItems (filterList:ArrayList<Specification>){
        filters = filterList
    }

    override fun onCheckItemSelected(filterValues: SpecValue) {
        this.filterValues.add(filterValues)
    }

    override fun onCheckItemDeSelected(filterValues: SpecValue) {
        this.filterValues.remove(filterValues)
    }

}