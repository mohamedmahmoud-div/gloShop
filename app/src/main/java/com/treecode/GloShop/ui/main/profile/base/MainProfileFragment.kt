package com.treecode.GloShop.ui.main.profile.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.login.AccountUser
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.profile.*
import com.treecode.GloShop.ui.main.profile.addressbook.MyAdressBookFragment
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.SessionManager
import com.treecode.GloShop.util.UserAccountManger
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.android.synthetic.main.fragment_main_profile.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mGoogleSignInClient: GoogleSignInClient

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
        val userAccount:AccountUser? = userManger.getAccountUSer()
        if (userAccount != null){
            text_member_name.text = userAccount.fullName
            text_member_account.text = userAccount.email
            text_member_account.visibility =View.VISIBLE
            text_member_name.visibility = View.VISIBLE
            btn_login_sign_up.visibility = View.GONE
            text_welcome.visibility = View.GONE
        }else{
            text_member_account.visibility =View.GONE
            text_member_name.visibility = View.GONE
            btn_login_sign_up.visibility = View.VISIBLE
            text_welcome.visibility = View.VISIBLE
        }


        layout_all_my_order_profile.setOnClickListener{
            val myAllorderFragment = MyAllorderFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(myAllorderFragment)


        }
        layout_pending_profile.setOnClickListener{
            val pendingShipmentFragment = PendingShipmentFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(pendingShipmentFragment)
        }

        layout_my_address_book_profile.setOnClickListener{
            val myAddressFragment = MyAdressBookFragment.newInstance(true)
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(myAddressFragment)
        }
        layout_wish_profile.setOnClickListener {
            val wishListFragment = WishListFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(wishListFragment)
        }
        layout_logout.setOnClickListener {
            loading.visibility =  View.VISIBLE
            val  sessionManager = SessionManager(requireContext())
            sessionManager.logout()
            val fc :LogoutListener? = activity as LogoutListener?
            fc?.onClickLogout()
            if (AccessToken.getCurrentAccessToken() != null) {
                GraphRequest(
                    AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE,
                    GraphRequest.Callback {
                        AccessToken.setCurrentAccessToken(null)
                        LoginManager.getInstance().logOut()
                        loading.visibility =  View.GONE

                    }
                ).executeAsync()
            }
            setupGoogle()
        }
        layout_polices.setOnClickListener {
            val policesFragment = PolicesFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(policesFragment)
        }
        layout_about_us.setOnClickListener {
            val aboutFragment = AboutUSFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(aboutFragment)
        }
        layout_contact_us.setOnClickListener {
            val contactUSFragment = ContactUSFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
            fc?.replaceFragment(contactUSFragment)
        }
    }
    private fun setupGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.GoogleID)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        //loading.visibility =  View.GONE

        mGoogleSignInClient.signOut()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_profile, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun dismissFragment() {
        val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        //fc?.replaceFragment(this)
    }
}