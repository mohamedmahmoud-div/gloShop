package com.treecode.GloShop.ui.main.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mvvmcoorutines.data.api.ApiService
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.login.AccountUser
import com.treecode.GloShop.data.model.login.LoginResponse
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.profile.addressbook.MyAdressBookFragment
import com.treecode.GloShop.util.SessionManager
import com.treecode.GloShop.util.UserAccountManger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_update_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var apiInterface: ApiService
/**
 * A simple [Fragment] subclass.
 * Use the [UpdateUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateUserFragment : Fragment() {
    private lateinit var updateCall: Call<LoginResponse>

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

        val userManger = UserAccountManger(requireContext())
        val userAccount: AccountUser? = userManger.getAccountUSer()
        val storeName = userAccount!!.storeName!!
        edit_update_store_name.setText(storeName)
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        btn_update_store_name.setOnClickListener {
            val storeName = edit_update_store_name.text.toString()
            if (storeName.isNullOrEmpty()){
                Toasty.warning(requireContext(),getString(R.string.invalid_store_name),Toasty.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val userManger = UserAccountManger(requireContext())
            val userAccount: AccountUser? = userManger.getAccountUSer()
            val userid = userAccount!!.id!!
            val sessionManger = SessionManager(requireContext())
            val token = userAccount.token!!
                val tokenToSend = "Token $token"
             updateCall = apiInterface.updateProfile(storeName=storeName,userID = userid,token = tokenToSend)
            loading.visibility = View.VISIBLE
            updateCall.enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: retrofit2.Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    loading.visibility = View.GONE

                    val erroBody =  response.errorBody().toString()
                    val header = response.headers()
                    val loginResponse: LoginResponse? = response.body()
                    val text: String? = loginResponse?.message
                    if (text != null && text == ""){
                    val data = loginResponse.data
                        userManger.saveUser(data!!)
                        dismissFragment()
                        //showResponse(loginResponse)
                    } else if (text != null ){
                        Toasty.error(requireContext(), "$text", Toast.LENGTH_LONG, true).show();
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<LoginResponse?>,
                    t: Throwable
                ) {
                    loading.visibility = View.GONE

                    if (t.message == "Canceled"){
                        return
                    }
                    Toasty.error(requireContext(),getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG).show()
                    call.cancel()

                }
            })
        }
        }
    private fun dismissFragment() {
        val myAdressBookFragment = UserFragment()
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        fc?.replaceFragment(myAdressBookFragment)
    }
    override fun onPause() {
        super.onPause()
        if(updateCall != null){
            updateCall.cancel()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_user, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdateUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdateUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}