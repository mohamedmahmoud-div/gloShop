package com.treecode.GloShop.ui.main.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.profile.ContactUSResponse
import com.treecode.GloShop.data.model.profile.ContactUsData
import com.treecode.GloShop.data.model.profile.ContactUserMessageRespons
import com.treecode.GloShop.util.UserAccountManger
import com.example.mvvmcoorutines.data.api.ApiService
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_contact_u_s.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ContactUSFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactUSFragment : Fragment() {
    private var facebookLink: String? = null
    private var  youtubeLink  : String? = null
    private var  googleLink     : String? = null
    private var  twitterLink  : String? = null
    private var  instgramLink : String? = null

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
   val FACEBOOK_URL = "https://www.facebook.com/YourPageName";
    val FACEBOOK_PAGE_ID = "YourPageName";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getContactUSData()
        val userManger =UserAccountManger(requireContext())
        val user = userManger.getAccountUSer()
        if(user != null) {
            text_member_name.visibility = View.VISIBLE
            text_member_account.visibility = View.VISIBLE
            text_member_name.text = user.fullName
            text_member_account.text = user.email
        }

        btn_send_comment.setOnClickListener {
            isInputsValid { error, isValid ->
                if (!isValid) {
                    Toasty.warning(requireContext(),error!!,Toasty.LENGTH_LONG).show()
                    return@isInputsValid
                } else {
                    val userName = ed_contact_us_name_buyer.text.toString()
                    val userEmail = ed_contact_us_email_buyer.text.toString()
                    val message = ed_contact_us_message_buyer.text.toString()
                    sendMessageToSeller(userName,userEmail,message)
                }
            }

        }
        btn_fb_contact.setOnClickListener {
            startActivity(getOpenFacebookIntent())
        }
        btn_youtube_contact.setOnClickListener {
            startActivity( Intent(
                Intent.ACTION_VIEW,
                Uri.parse(youtubeLink!!)
            ))
        }
        btn_instgram_contact.setOnClickListener {
            startActivity(getOpenInstgramIntent())

        }
        btn_google_contact.setOnClickListener {
            Toasty.success(requireContext(),"btn_google_contact").show()
            startActivity( Intent(
                Intent.ACTION_VIEW,
                Uri.parse(googleLink!!)
            ))
        }
        btn_twitter_contact.setOnClickListener {
            Toasty.success(requireContext(),"btn_twitter_contact").show()
            startActivity( Intent(
                Intent.ACTION_VIEW,
                Uri.parse(twitterLink!!)
            ))
        }


    }
    private fun sendMessageToSeller(userName:String,userEmail:String,message: String){
        val apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        val sendMessageCall = apiInterface.sendMessageToSeller(userName,email = userEmail,message = message)
        progress_circular_contact_us.visibility = View.VISIBLE
        sendMessageCall.enqueue(object :Callback<ContactUserMessageRespons> {
            override fun onFailure(call: Call<ContactUserMessageRespons>, t: Throwable) {
                progress_circular_contact_us.visibility = View.GONE

                Toasty.error(requireContext(),getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG).show()
                call.cancel()
            }

            @SuppressLint("StringFormatInvalid")
            override fun onResponse(
                call: Call<ContactUserMessageRespons>,
                response: Response<ContactUserMessageRespons>
            ) {
                progress_circular_contact_us.visibility = View.GONE
                val contactUsResponse = response.body()
                if(contactUsResponse != null){
                    val contactData = contactUsResponse.data
                    if (contactData != null){
                        Toasty.success(requireContext(),getString(R.string.message_send_success,Toasty.LENGTH_LONG,true)).show()
                    }else {
                        val errors = contactUsResponse.errors
                        if (!errors.isNullOrEmpty()){
                            Toasty.error(requireContext(),errors[0],Toasty.LENGTH_LONG,true).show()
                        }
                    }
                }else {
                    Toasty.error(requireContext(),getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG).show()
                }
            }

        })
    }
    private fun dismissFragment(){

    }
    fun getOpenFacebookIntent(): Intent? {
        return try {
            context?.packageManager?.getPackageInfo("com.facebook.katana", 0)
            Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink!!))
        } catch (e: Exception) {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(facebookLink!!)
            )
        }
    }
    fun getOpenInstgramIntent():Intent? {
        return try {
            requireContext().packageManager.getPackageInfo("com.instagram.android", 0)
            Intent(Intent.ACTION_VIEW, Uri.parse(instgramLink))
        } catch (e: Exception) {
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(instgramLink!!)
            )
        }
    }
    private fun isInputsValid(callback:(error:String?,isValid:Boolean)-> Unit){
        val userName = ed_contact_us_name_buyer.text.toString()
        val userEmail = ed_contact_us_email_buyer.text.toString()
        val message = ed_contact_us_message_buyer.text.toString()
if (userName.isNullOrEmpty()){
    callback(getString(R.string.invalid_fullname),false)
return
}
        if (userEmail.isNullOrEmpty()|| !userEmail.contains("@") ||  !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            callback(getString(R.string.invalid_email),false)
            return
        }
        if (message.isNullOrEmpty()){
            callback(getString(R.string.empty_message),false)
            return
        }

callback(null,true)
    }
    private fun getContactUSData(){
        val apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
        val getContactUSCall = apiInterface.getContactUS()
        progress_circular_contact_us.visibility = View.VISIBLE
        getContactUSCall.enqueue(object: Callback<ContactUSResponse> {
            override fun onFailure(call: Call<ContactUSResponse>, t: Throwable) {
                progress_circular_contact_us.visibility = View.GONE

                Toasty.error(requireContext(),"Check Internet Connection").show()
                call.cancel()
            }

            override fun onResponse(
                call: Call<ContactUSResponse>,
                response: Response<ContactUSResponse>
            ) {
                progress_circular_contact_us.visibility = View.GONE
                    if(response.code() == 200){
                        val contactUSResponse = response.body()!!
                     val contactUSData = contactUSResponse.data
                        if (contactUSData.isNullOrEmpty())
                            Toasty.error(requireContext(),"Check Internet Connection").show()
                        else {
                            val contactUs = contactUSData[0]!!
                                setupUI(contactUs)
                        }
                    }else {
                        Toasty.error(requireContext(),"Check Internet Connection").show()

                    }
            }

        })
    }
    private fun setupUI(contactUs:ContactUsData){
        val email = contactUs.email
        val address = contactUs.address
        val phone = contactUs.phone
        text_seller_email.text = email
        text_seller_address.text = address
        text_seller_phone.text = phone

         facebookLink = contactUs.faceBookLink
         youtubeLink = contactUs.youtubeLink
         googleLink = contactUs.googleLink
         twitterLink = contactUs.twitterLink
         instgramLink = contactUs.insgragmLink
        if (!facebookLink.isNullOrEmpty()){
            btn_fb_contact.isEnabled = true
            btn_fb_contact.visibility = View.VISIBLE

        }
        if (!youtubeLink.isNullOrEmpty()){
            btn_youtube_contact.isEnabled = true
            btn_youtube_contact.visibility = View.VISIBLE
        }
        if(!googleLink.isNullOrEmpty()){
            btn_google_contact.isEnabled = true
            btn_google_contact.visibility = View.VISIBLE
        }
        if(!twitterLink.isNullOrEmpty()){
            btn_twitter_contact.isEnabled = true
            btn_twitter_contact.visibility = View.VISIBLE
        }
        if(!instgramLink.isNullOrEmpty()){
            btn_instgram_contact.isEnabled = true
            btn_instgram_contact.visibility = View.VISIBLE
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_u_s, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ContactUSFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ContactUSFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}