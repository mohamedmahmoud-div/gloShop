package com.treecode.GloShop.ui.adapter.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.search.SpecValue
import kotlinx.android.synthetic.main.item_filter_value.view.*

class FilterValuesAdapter(private val filtervalues:ArrayList<SpecValue>, private val rollBack: ParentRecyclerCallBack) :RecyclerView.Adapter<FilterValuesAdapter.DataViewHolder>() {
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var filterValuesSelected = HashSet<SpecValue>()
        fun bind(filterValue: SpecValue) {
            itemView.text_filter_value.text = filterValue.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        FilterValuesAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_filter_value, parent, false)
        )
    override fun getItemCount(): Int {
return  filtervalues.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val filterValue = filtervalues[position]
        holder.bind(filterValue)
        holder.itemView.text_filter_value.setOnClickListener(View.OnClickListener {
            if (holder.itemView.text_filter_value.isChecked) {
               holder.filterValuesSelected.add(filterValue)
                rollBack.onCheckItemSelected(filterValue)
            } else {
                holder.filterValuesSelected.remove(filterValue)

                rollBack.onCheckItemDeSelected(filterValue)
            }
        })
    }
}

interface ParentRecyclerCallBack {
    fun onCheckItemSelected(filterValues:SpecValue)
    fun onCheckItemDeSelected(filterValues:SpecValue)
}