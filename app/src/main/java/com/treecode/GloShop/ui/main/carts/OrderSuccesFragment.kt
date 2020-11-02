package com.treecode.GloShop.ui.main.carts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.profile.MyAllorderFragment
import com.treecode.GloShop.util.CartsManger
import kotlinx.android.synthetic.main.activity_order_succes.*
import kotlinx.android.synthetic.main.reuse_curve_text_image_button.*

class OrderSuccesFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_order_succes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val carManger = CartsManger(requireContext())
        carManger.emptyCart()
        btn_place_order.text = getString(R.string.my_order)
        val toolbar = requireActivity().findViewById(R.id.carts_bar) as Toolbar?
        if (toolbar != null){
            toolbar.visibility = View.GONE
        }else{

        }
        val cartsFragment = CartsFragment()
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?

        btn_close.setOnClickListener {

            fc?.popFragment(cartsFragment)
          //  fc?.replaceFragment(cartsFragment)
        }
        btn_to_allOrders.setOnClickListener {
            val myorderFragmment = MyAllorderFragment()
            fc?.popFragment(cartsFragment)
            fc?.replaceFragment(myorderFragmment)
        }
    }
}