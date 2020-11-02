package com.treecode.GloShop.data.model.search

class SearchQueryRequest {
    var cateogry:Int? = null
    var collection:Int? = null
    var searchText :String? = null
    var orderingSort : String = "" // price OR -price
}