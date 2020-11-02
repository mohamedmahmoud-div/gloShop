package com.treecode.GloShop.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.profile.Order
import com.treecode.GloShop.data.model.profile.OrderResponse
import com.treecode.GloShop.ui.adapter.profile.MyOrdersAdapter
import com.treecode.GloShop.util.SessionManager
import com.example.mvvmcoorutines.data.api.ApiService
import kotlinx.android.synthetic.main.fragment_my_allorder.*
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyAllorderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAllorderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var myOrders = ArrayList<Order>()
private lateinit var myOrdersAdapter:MyOrdersAdapter
    private lateinit var apiInterface: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        progress_circular_my_order.visibility= View.VISIBLE
        getOrdersFromNetwork()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_allorder, container, false)
    }


    private fun setupUI(){
        val toolbar = requireActivity().findViewById(R.id.carts_bar) as Toolbar?
        if (toolbar != null){
            toolbar.visibility = View.GONE
        }else{

        }

myOrdersAdapter = MyOrdersAdapter(myOrders){orderID ->
    // todo request to product Details and navigate
}
        val  verticalLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recycler_all_orders.layoutManager = verticalLayoutManager
        recycler_all_orders.adapter = myOrdersAdapter
    }

    private fun getOrdersFromNetwork(){
        val sessionManager = SessionManager(requireContext())
        val token = "Token "+ sessionManager.fetchAuthToken()!!
        val productDetailsCall = apiInterface.getDeliverdOrders(token)

        productDetailsCall.enqueue(object : Callback<OrderResponse?> {
            override fun onResponse(
                call: retrofit2.Call<OrderResponse?>,
                response: Response<OrderResponse?>
            ) {
                progress_circular_my_order.visibility= View.GONE

                val erroBody = response.errorBody().toString()
                val header = response.headers()
                val orderResponse: OrderResponse? = response.body()
                val text: String? = orderResponse?.message
                if (orderResponse != null){
                    val orders = orderResponse.orders
                    if (!orders.isNullOrEmpty()){
                        text_orders_empty.visibility = View.GONE

                    updateRecyclerView(orders)
                    }else{
                        // todo there is no orders
                        text_orders_empty.visibility = View.VISIBLE
                    }

                }
                if (text != null && text == "") {
                }

            }

            override fun onFailure(
                call: retrofit2.Call<OrderResponse?>,
                t: Throwable
            ) {
                progress_circular_my_order.visibility= View.GONE

                call.cancel()
            }
        })
    }
    private fun  updateRecyclerView(orders:ArrayList<Order>){
        myOrdersAdapter.addOrders(orders)
        myOrdersAdapter.notifyDataSetChanged()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAllorderFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyAllorderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}