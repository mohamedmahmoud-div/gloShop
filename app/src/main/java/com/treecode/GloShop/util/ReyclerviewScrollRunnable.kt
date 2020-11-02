package com.treecode.GloShop.util

class ReyclerviewScrollRunnable: Runnable {
    var count = 0
    var flag = true
var adapterSize:Int = 0
constructor(adapterSize:Int){
    this.adapterSize = adapterSize
}
    override fun run() {

        if (count < adapterSize) {
            if (count == adapterSize - 1) {
                flag = false
            } else if (count == 0) {
                flag = true
            }
            if (flag) count++ else count--
          //  recyclerview_deal_horizontal.smoothScrollToPosition(count)
      //      handler.postDelayed(this, 4000)
        }
    }
}