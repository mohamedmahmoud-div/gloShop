package com.treecode.GloShop.ui.main.profile.addressbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.profile.*
import com.treecode.GloShop.ui.adapter.profile.AddressBookAdapter
import com.treecode.GloShop.ui.main.carts.CheckoutFragment
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.profile.base.MainProfileFragment
import com.treecode.GloShop.util.AddressManger
import com.treecode.GloShop.util.SessionManager
import com.example.mvvmcoorutines.data.api.ApiService
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_my_adress_book.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashSet

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyAdressBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyAdressBookFragment : Fragment() {
    private  var addresBook: HashSet<CreateLocationRequest>? = null
    private lateinit var apiInterface: ApiService

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var isFromProfileNavigation = false
    private lateinit var addressManger: AddressManger
    private lateinit var addressAdapter :AddressBookAdapter
private var listOFAddresBook = HashSet<AddressBook>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isFromProfileNavigation = it.getBoolean(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_adress_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addressManger = AddressManger(requireContext())
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        val toolbar = requireActivity().findViewById(R.id.carts_bar) as Toolbar?
        if (toolbar != null){
            toolbar.visibility = View.GONE
        }else{

        }

        val sessionManager = SessionManager(requireContext())
        val token = sessionManager.fetchAuthToken()
        if (token == null){
            text_address_book_empty.text = "Please Login"
            btn_add_address.visibility = View.GONE
        }
        addressAdapter = AddressBookAdapter(listOFAddresBook,{
            val pendingShipmentFragment = AddAdressLocationFragment.newInstance(it.id,it.phone,it.streetAddress,it.region)

            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(pendingShipmentFragment)
        },{ address, postion ->
            listOFAddresBook.remove(address)
            addressAdapter.remove(address)
            addressAdapter.notifyItemChanged(postion)

            deleteAddress(address.id)
        },{defaultAddress ->
            Toasty.success(requireContext(),getString(R.string.address_selected))
            val addressManger = AddressManger(requireContext())
            addressManger.setDefaultAddressBook(defaultAddress)
            popFragment()
        })


        val  verticalLayoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        recycler_address_book.layoutManager = verticalLayoutManager
        recycler_address_book.adapter = addressAdapter

        getAddressBookFromNetwork()
         //addresBook = addressManger.getAddressBook()
    /*   if(addresBook.isNullOrEmpty()) {
           recycler_address_book.visibility = View.GONE
           text_address_book_empty.visibility = View.VISIBLE
       } else{
           recycler_address_book.visibility = View.VISIBLE
           text_address_book_empty.visibility = View.GONE
       }*/
        btn_add_address.setOnClickListener {
            val addresFragment = AddAdressLocationFragment()
            addresFragment.isFromProfile = false
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(addresFragment)
        }
    }
    private fun popFragment(){
        if(!isFromProfileNavigation){
            val cartFragment = CheckoutFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(cartFragment)
        } else {
            val mainProfileFragment = MainProfileFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(mainProfileFragment)
        }

    }
    private fun deleteAddress(addressID:Int){
        val sessionManager = SessionManager(requireContext())
        val addressManger = AddressManger(requireContext())
        val defaultAddress = addressManger.getDefaultAddress()
      if (defaultAddress != null){
          if (defaultAddress!!.id == addressID){
              addressManger.removeDefaultAddress()
          }

      }
        if(listOFAddresBook.isNullOrEmpty()) {
            recycler_address_book.visibility = View.GONE
            text_address_book_empty.visibility = View.VISIBLE
        }
            val token = sessionManager.fetchAuthToken()
        if (token != null){
            val tokenToSent = "Token $token"
            val deleteCall = apiInterface.deleteAddressBook(addressID,tokenToSent)
            loading.visibility = View.VISIBLE

            deleteCall.enqueue(object:Callback<LocationRepsonse> {
                override fun onFailure(call: Call<LocationRepsonse>, t: Throwable) {
                    loading.visibility = View.GONE

                    call.cancel()
                }

                override fun onResponse(
                    call: Call<LocationRepsonse>,
                    response: Response<LocationRepsonse>
                ) {
                    loading.visibility = View.GONE

                if (response.code() == 200){

                }else if (response.code() == 404){

                    Toasty.warning(requireContext(),"Pleas Login First").show()
                }
                }

            })

        }else {
            Toasty.warning(requireContext(),"Please Login First").show()
        }
    }
private fun getAddressBookFromNetwork(){
    loading.visibility = View.VISIBLE
    val sessionManager = SessionManager(requireContext())
    val token = sessionManager.fetchAuthToken()
    if (token != null) {
        val tokenSend = "Token $token"
        val countriesCall = apiInterface.getAddressBook(tokenSend)
        countriesCall.enqueue(object : Callback<AddressBookResponse?> {
            override fun onFailure(call: Call<AddressBookResponse?>, t: Throwable) {
                loading.visibility = View.GONE

                Toasty.error(requireContext(),getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG,true).show()
                call.cancel()

            }

            override fun onResponse(
                call: Call<AddressBookResponse?>,
                response: Response<AddressBookResponse?>
            ) {
                loading.visibility = View.GONE

                val countrResponse: AddressBookResponse? = response.body()
                if (countrResponse != null){
                        setupList(countrResponse.data)
        
                }
                val text: String? = countrResponse?.message
                if (text != null && text != ""){
                    //      Toasty.error(requireContext(), "$text", Toast.LENGTH_LONG, true).show();

                } else if (text != null ){
                }
            }

        })
    }
}
    private fun setupList(listOfAddress:HashSet<AddressBook>){

        listOFAddresBook = listOfAddress
        if(listOFAddresBook.isNullOrEmpty()) {
            recycler_address_book.visibility = View.GONE
            text_address_book_empty.visibility = View.VISIBLE
        } else{
            addressAdapter.replaceItems(listOfAddress)
            addressAdapter.notifyDataSetChanged()
            recycler_address_book.visibility = View.VISIBLE
            text_address_book_empty.visibility = View.GONE
        }

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyAdressBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(isFromProfile:Boolean) =
            MyAdressBookFragment().apply {
                arguments = Bundle().apply {
                 putBoolean(ARG_PARAM1,isFromProfile)
                }
            }
    }
}