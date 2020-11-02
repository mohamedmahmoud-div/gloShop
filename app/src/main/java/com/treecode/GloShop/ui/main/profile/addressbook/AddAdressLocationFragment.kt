package com.treecode.GloShop.ui.main.profile.addressbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.profile.*
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.util.AddressManger
import com.treecode.GloShop.util.SessionManager
import com.example.mvvmcoorutines.data.api.ApiService
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_adress_location.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_ID= "paramID"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "param3"


/**
 * A simple [Fragment] subclass.
 * Use the [AddAdressLocationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("UNREACHABLE_CODE")
class AddAdressLocationFragment : Fragment() {
    private lateinit var countryAdapter: ArrayAdapter<String>
    private lateinit var citiesAdapter: ArrayAdapter<String>
private var editableAddressBook:AddressBook? = null
    // TODO: Rename and change types of parameters
    private var phone: String? = null
    private var streetAddress: String? = null
    private var region: String? = null
    var countries = ArrayList<Country>()
    var cites = ArrayList<City>()
    var countriesNames = ArrayList<String>()
    var citiesName=ArrayList<String>()
    var addressID:Int? = null
    private lateinit var apiInterface: ApiService
    private lateinit var sessionManager: SessionManager
private  var  countrySelected:Country? = null
private  var  citySelected:City? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            phone = it.getString(ARG_PARAM1)
            addressID = it.getInt(ARG_ID)
            streetAddress = it.getString(ARG_PARAM2)
            region = it.getString(ARG_PARAM3)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_adress_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        getCountriesFromNetwork()
        setupUI()
        btn_save_address.setOnClickListener {

            val address = edit_payment_address.text
            val region = edit_address_region.text
            val phoneNumber = edit_address_phone.text
            if (addressID != null){
                // patch api
                try {
                    val postCode = edit_address_post_code.text.toString().toInt()
                    if (!address.isNullOrEmpty() &&!region.isNullOrEmpty() && !phoneNumber.isNullOrEmpty() && phoneNumber.toString().count() == 11  && countrySelected != null){
                        updateAddress()
                    }else{
                        Toasty.error(requireContext(),"Please Fill All Data",Toasty.LENGTH_LONG).show()
                    }
                }catch (e:Exception){
                    Toasty.error(requireContext(),"Please enter valid Post code",Toasty.LENGTH_LONG).show()
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
                }

            }else {
                try {
                    val postCode = edit_address_post_code.text.toString().toInt()
                    if (!address.isNullOrEmpty() &&!region.isNullOrEmpty() && !phoneNumber.isNullOrEmpty() && phoneNumber.toString().count() == 11 ){
                        sendAddressToServer()
                    }else{
                        Toasty.error(requireContext(),"Please Fill All Data",Toasty.LENGTH_LONG).show()
                    }
                }catch (e:Exception){
                    Toasty.error(requireContext(),"Please enter valid Post code",Toasty.LENGTH_LONG).show()
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
                }


            }

        }
        // todo request to Server to get cities and countries
    }
    private fun updateAddress(){
        val address = edit_payment_address.text.toString()

        val postCode = edit_address_post_code.text.toString().toInt()

        val region = edit_address_region.text.toString()
        val phoneNumber = edit_address_phone.text.toString()
        val countryID = countrySelected!!.id
        val cityID = citySelected!!.id
        val addressShip = CreateLocationRequest(countryID =countryID ,cityId =cityID,streetAddress = address,postCode = postCode,phoneNumber = phoneNumber,region = region,id = 0)
        val sessionManager = SessionManager(requireContext())
        val token = sessionManager.fetchAuthToken()
        if (token == null){
            Toasty.error(requireContext(),"Please Login First").show()
            return
        }
        val tokenToSend = "Token $token"
        if(addressID != null){
            val updateCall = apiInterface.updateAddressBook(addressID!!,addressShip,tokenToSend)
                updateCall.enqueue(object :Callback<LocationRepsonse>{
                    @SuppressLint("CheckResult")
                    override fun onFailure(call: Call<LocationRepsonse>, t: Throwable) {
                        Toasty.error(requireContext(),getString(R.string.please_check_internet_connection)).show()
                        call.cancel()
                    }

                    override fun onResponse(
                        call: Call<LocationRepsonse>,
                        response: Response<LocationRepsonse>
                    ) {
                        if (response.code() == 404){
                           val errorBody =  response.errorBody()
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Toasty.warning(
                                requireContext(),
                                jObjError.getString("message"),
                                Toast.LENGTH_LONG
                            ).show()
                        }else if (response.code() == 200){
                            val locationResponse: LocationRepsonse? = response.body()
//                            dismissFragment()
//                            if (locationResponse != null){
//
//                            }
                            val text: String? = locationResponse?.message
                            if (text != null && text == ""){
                                dismissFragment()
                                saveAddress(locationResponse.data)

                            } else if (text != null ){
                                Toasty.error(requireContext(), "$text", Toast.LENGTH_LONG, true).show();
                            }
                        }

                    }


                })

        }
    }
    private fun setupUI(){
        if(phone !=null ){
            edit_payment_address.setText(streetAddress)
            edit_address_region.setText(region)
            edit_address_phone.setText(phone)
        }
      countriesNames = countries.map { country ->
          country.name
      }   as ArrayList<String>
        citiesName = cites.map {
            it.name
        } as ArrayList<String>


         countryAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, countriesNames)
            spinner_country.adapter = countryAdapter
         citiesAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, citiesName)
        spinner_city.adapter = citiesAdapter
            spinner_country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
                        countrySelected = countries[0]
                    }

                override fun onItemSelected(p0: AdapterView<*>?, view: View?, postion: Int, p3: Long) {
                    citiesAdapter.clear()
                    countrySelected = countries[postion]
                    cites = countrySelected!!.cities
                    citiesName = cites.map {
                        it.name
                    } as ArrayList<String>
                        citiesAdapter.addAll(citiesName)
                }

            }
spinner_city.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {
                citySelected = countries[0].cities[0]

    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, postion: Int, p3: Long) {
        citySelected = cites[postion]
    }

}

    }
    private fun isPhoneValid(number:String):Boolean{
     val regex = "(201)[0-9]{9}"
        val pattern: Pattern = Pattern.compile(regex)

        val matcher: Matcher = pattern.matcher(number)
        return  matcher.matches()
    }

private fun getCountriesFromNetwork(){
    sessionManager = SessionManager(requireContext())
val token = "Token "+ sessionManager.fetchAuthToken()!!
    val countriesCall = apiInterface.getCountries(token)
    countriesCall.enqueue(object :Callback<CountryAddressResonse?>{
        override fun onFailure(call: Call<CountryAddressResonse?>, t: Throwable) {
            Toasty.error(requireContext(),getString(R.string.please_check_internet_connection)).show()
            call.cancel()

        }

        override fun onResponse(
            call: Call<CountryAddressResonse?>,
            response: Response<CountryAddressResonse?>
        ) {
            val countrResponse: CountryAddressResonse? = response.body()
                if (countrResponse != null){
                    countrResponse.countries?.let { updateSpinners(it) }
                }
            val text: String? = countrResponse?.message
            if (text != null && text != ""){
          //      Toasty.error(requireContext(), "$text", Toast.LENGTH_LONG, true).show();

            } else if (text != null ){
            }
        }

    })
}
    private fun updateSpinners(countries:ArrayList<Country>){
        this.countries = countries
        countriesNames = countries.map {
            it.name
        } as ArrayList<String>
        countryAdapter.addAll(countriesNames)
        val firstCountry = countries[0]
        countrySelected = firstCountry
        val citiesInFirstCountry =firstCountry.cities
        citySelected = citiesInFirstCountry[0]
        this.cites = citiesInFirstCountry
        citiesName = citiesInFirstCountry.map {
            it.name
        }as ArrayList<String>
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param phone Parameter 1.
         * @param streetAddress Parameter 2.
         * @return A new instance of fragment AddAdressLocationFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(addressID:Int,phone: String, streetAddress: String, region:String) =
            AddAdressLocationFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, phone)
                    putInt(ARG_ID, addressID)
                    putString(ARG_PARAM2, streetAddress)
                    putString(ARG_PARAM3, region)
                }
            }
    }
    private fun sendAddressToServer(){

        val address = edit_payment_address.text.toString()

        val postCode = edit_address_post_code.text.toString().toInt()
        val region = edit_address_region.text.toString()
        val phoneNumber = edit_address_phone.text.toString()

        if(countrySelected ==null || citySelected == null){
            Toasty.error(requireContext(),getString(R.string.select_country),Toasty.LENGTH_LONG).show()
        }else{
            val countryID = countrySelected!!.id
            val cityID = citySelected!!.id
            val addressShip = CreateLocationRequest(countryID =countryID ,cityId =cityID,streetAddress = address,postCode = postCode,phoneNumber = phoneNumber,region = region,id = 0)
            val sessionManager = SessionManager(requireContext())
            val token = "Token " + sessionManager.fetchAuthToken()
            token?.let {
             val callAddAdress =    apiInterface.addAddress(addressShip, it)
                callAddAdress.enqueue(object :Callback<LocationRepsonse?>{
                    override fun onFailure(call: Call<LocationRepsonse?>, t: Throwable) {

                        Toasty.error(requireContext(),getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG).show()
                        call.cancel()

                    }

                    override fun onResponse(
                        call: Call<LocationRepsonse?>,
                        response: Response<LocationRepsonse?>
                    ) {
                        val locationResponse: LocationRepsonse? = response.body()
                        val text: String? = locationResponse?.message
                        if (text != null && text == ""){
                            dismissFragment()
                            saveAddress(locationResponse.data)

                        } else if (text != null ){
                            val errors = locationResponse.error
                            if (!errors.isNullOrEmpty()){
                                Toasty.error(requireContext(), errors[0], Toast.LENGTH_LONG, true).show();

                            }
                        }

                    }

                }


                )
            }

        }

    }

    private fun dismissFragment() {
        val myAdressBookFragment = MyAdressBookFragment.newInstance(true)
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(myAdressBookFragment)
    }

    private fun saveAddress(address: CreateLocationRequest){
        val addressManger = AddressManger(requireContext())
      //  addressManger.saveAddress(address = address)
    }

}