package com.treecode.GloShop.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.treecode.GloShop.LaunchActivity
import com.treecode.GloShop.R
import com.treecode.GloShop.ui.main.BaseActivity
import com.treecode.GloShop.ui.main.home.ui.main.ActivityFragmentChangeListener
import com.treecode.GloShop.ui.main.profile.base.MainProfileFragment
import kotlinx.android.synthetic.main.fragment_change_language.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeLanguageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class ChangeLanguageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var currentLanguage = "en"
    lateinit var locale: Locale
    private var languageChange = "en"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      currentLanguage =   Locale.getDefault().getDisplayLanguage();
        layout_english.setOnClickListener {
            languageChange = "en"
            text_arabic.setBackgroundColor((resources.getColor(R.color.my_transparent)))
            text_english.setBackgroundColor((resources.getColor(R.color.colorPrimary)))
            text_english.setTextColor(resources.getColor(R.color.white))
            text_arabic.setTextColor(resources.getColor(R.color.black))

        }
        layout_arabic.setOnClickListener {
            languageChange =  "ar"
            text_arabic.setBackgroundColor((resources.getColor(R.color.colorPrimary)))
            text_arabic.setTextColor(resources.getColor(R.color.white))
            text_english.setBackgroundColor((resources.getColor(R.color.my_transparent)))
            text_english.setTextColor(resources.getColor(R.color.black))

        }
        btn_apply_language.setOnClickListener {
            setLocale(languageChange)
        }
    }
    private fun setLocale(localeName: String) {
//        var change = ""
//
      val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        if (localeName == "العربية") {
//            change="ar"
//        } else if (localeName=="en" ) {
//            change = "en"
//        }else {
//            change =""
//        }
        sharedPreferences.edit().putString("language",localeName).apply()
        BaseActivity.dLocale = Locale(localeName)
        //BaseActivity.dLocale
        BaseActivity.home = null
      //  RetrofitBuilder.getRetrofit().create(ApiService::class.java)

//
//        if (localeName != currentLanguage) {
//            locale = Locale(localeName)
//            val res = resources
//            val dm = res.displayMetrics
//            val conf = res.configuration
//            conf.locale = locale
//            res.updateConfiguration(conf, dm)
            val mainProfileFragment = MainProfileFragment()
            val fc: ActivityFragmentChangeListener? = activity as ActivityFragmentChangeListener?
        val inent = Intent(requireContext(), LaunchActivity::class.java)
        activity?.startActivity(inent)
        activity?.finish()
           // fc?.replaceFragment(mainProfileFragment)

    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ChangeLanguageFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangeLanguageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}