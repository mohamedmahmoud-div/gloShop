package com.treecode.GloShop.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.profile.PoiicesResonse
import com.treecode.GloShop.util.Constants
import com.example.mvvmcoorutines.data.api.ApiService
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_polices.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PolicesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PolicesFragment : Fragment() {
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
        getPolicesData()
    }
    private fun getPolicesData(){
        val apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        val aboutUsCall = apiInterface.getPolicesORAbout(Constants.URL_POLICES)
        aboutUsCall.enqueue(object : Callback<PoiicesResonse> {
            override fun onFailure(call: Call<PoiicesResonse>, t: Throwable) {

                Toasty.error(requireContext(),"Check Internet Connection").show()
            }

            override fun onResponse(call: Call<PoiicesResonse>, response: Response<PoiicesResonse>) {
                val poiicesResonse = response.body()
                if (poiicesResonse != null){
                    val policesData = poiicesResonse.data
                    text_polices_data.text = policesData.details
                }
            }

        })

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_polices, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PolicesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PolicesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}