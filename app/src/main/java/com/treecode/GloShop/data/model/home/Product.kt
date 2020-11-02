package com.treecode.GloShop.data.model.home

import com.treecode.GloShop.data.model.search.Specification
import com.treecode.GloShop.ui.main.search.SelectedSpec
import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2020 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


 class Product(

	 @SerializedName("id") val id: Int,
	 @SerializedName("name") val name: String,
	 @SerializedName("price") val price: Int,
	 @SerializedName("offer") val offer: OptionalOffer?,
	 @SerializedName("discount") val discount: Int?,
	 @SerializedName("main_img") val main_img: String,
	 @SerializedName("stars") val stars: String?,
	 @SerializedName("specs") val hasSpecs: Boolean,
	 @SerializedName("availability") val availability:Int,
	 var specification: Specification?,


/*	@SerializedName("specs")
	var specs: ArrayList<Specification>?,*/
	var pieceCount:Int = 1,
	 @SerializedName("category")
	 var categoryName: ProductCategory,
	 var isCartSelected:Boolean = false,
	 var isWishSelected:Boolean= false

)
class OptionalOffer(
	@SerializedName("type") val type:String,
	@SerializedName("dicount") val discount: Double?,
	@SerializedName("after_price") val afterPrice:Int


)
class ProductCategory(
	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String
)
class  CartProduct(
	@SerializedName("product")
	val id: Int,
	@SerializedName("specs")
	var seletectedSpecValuesString: String = "",
	@SerializedName("quantity")
	var quantity:Int = 1,
	 val name: String,
	val categoryName:String,
	val price:Int,
	val totalPirce:Int,
	var selectedIDS :ArrayList<SelectedSpec>?,
	var totalAvailbilty:Int?,
	val main_img: String,
	val afterPrice: Int?
)