package com.treecode.GloShop.data.model.home

import com.treecode.GloShop.data.model.Cateogry
import com.treecode.GloShop.data.model.HotDeal
import com.google.gson.annotations.SerializedName




 class Home (

	 @SerializedName("hot_deals") val hot_deals : ArrayList<HotDeal>,
	 @SerializedName("collections") val collections : ArrayList<Cateogry>,
	 @SerializedName("new_arrivals") val new_arrivals : ArrayList<Department>,
	 @SerializedName("product_offers") val productsOffer : ArrayList<Offer>,
	 @SerializedName("top_selling") val top_selling : ArrayList<Department>
)