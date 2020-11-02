package com.treecode.GloShop.ui.adapter.productdetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.search.ProductReview
import kotlinx.android.synthetic.main.item_product_review.view.*

class ProductReviewAdapter(
    private  val reviews:ArrayList<ProductReview>
):RecyclerView.Adapter<ProductReviewAdapter.DataViewHolder>()
{
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(review: ProductReview) {
            itemView.text_buyer_review_name.text = review.buyer.name
            itemView.text_buyer_review_comment.text = review.comment
            itemView.text_buyer_rating_percent.text =  review.stars.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        ProductReviewAdapter.DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_review, parent, false)
        )
    override fun getItemCount(): Int {
return reviews.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
    val review = reviews[position]
        holder.bind(review)
    }
}