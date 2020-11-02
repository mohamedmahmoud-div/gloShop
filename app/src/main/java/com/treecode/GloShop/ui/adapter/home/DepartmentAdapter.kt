package com.treecode.GloShop.ui.adapter.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.Department
import com.treecode.GloShop.data.model.home.Product
import kotlinx.android.synthetic.main.item_department_layout.view.*


// TODO: 8/16/2020 this will Navigate to prdouct List

class DepartmentAdapter(
    private var categorires:ArrayList<Department>, private val listener: (ArrayList<Product>) -> Unit
): RecyclerView.Adapter<DepartmentAdapter.DataViewHolder>() {
    private var selected = ArrayList<Boolean>(categorires.size)
    var selectedPostion = 0

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(category: Department) {
            itemView.button_department.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_department_layout, parent, false)

        )

    override fun getItemCount(): Int {
        return categorires.count()
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val category = categorires[position]
        holder.bind(category)

       if (selectedPostion == position){
            holder.itemView.button_department.setTextColor(R.color.black)
            holder.itemView.image_department.visibility = View.VISIBLE
        }else{
           holder.itemView.button_department.setTextColor(R.color.black_faded)
           holder.itemView.image_department.visibility = View.GONE
       }
        holder.itemView.button_department.setOnClickListener{
         val periousSelectd = selectedPostion
            selectedPostion = position
            notifyItemChanged(periousSelectd)
            notifyItemChanged(selectedPostion)

            listener(category.products)

        }
//        holder.itemView.setOnClickListener {
//        }
    }
    fun addDepartments(departments: ArrayList<Department>){
        categorires = departments
        departments.forEach { department ->
            selected.add(false)
        }

    }


}
