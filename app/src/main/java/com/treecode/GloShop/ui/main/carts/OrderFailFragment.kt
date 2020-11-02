package com.treecode.GloShop.ui.main.carts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import kotlinx.android.synthetic.main.fragment_order_fail.*
import kotlinx.android.synthetic.main.reuse_curve_text_image_button.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OrderFailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OrderFailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_place_order.text = getText(R.string.mycart)
        val toolbar = requireActivity().findViewById(R.id.carts_bar) as Toolbar?
        if (toolbar != null){
            toolbar.visibility = View.GONE
        }
        btn_close_fail.setOnClickListener {
            navigateToMyCart()
        }
        btn_to_my_carts.setOnClickListener {
            navigateToMyCart()
        }
    }
    private fun navigateToMyCart(){
        val cartsFragment = CartsFragment()
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.popFragment(cartsFragment)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_fail, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OrderFailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrderFailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}