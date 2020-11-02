package com.treecode.GloShop.ui.main.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.treecode.GloShop.R
import com.treecode.GloShop.data.model.home.Product
import com.treecode.GloShop.data.model.profile.CreditCard
import com.treecode.GloShop.util.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_add_credit.*
import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCreditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCreditFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        edit_card_expire_add_payment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, start: Int, removed: Int, added: Int) {
                if (start == 1 && start+added == 2 && p0?.contains('/') == false) {
                    edit_card_expire_add_payment.setText(p0.toString() + "/")
                } else if (start == 3 && start-removed == 2 && p0?.contains('/') == true) {
                    edit_card_expire_add_payment.setText(p0.toString().replace("/", ""))
                }
            }
        })
        btn_save_credit_profile.setOnClickListener{
            val creditNumber = edit_card_number_add_payment.text.toString()
            val expireDate =  edit_card_expire_add_payment.text.toString()
            val cvv  = edit_card_cvv_add_payment.text.toString()
            val ownerName = edit_card_name_add_payment.text.toString()
if (isCreditNumberValid(creditNumber) && !isTextEmptyOrNull(expireDate)&&!isTextEmptyOrNull(cvv)&&!isTextEmptyOrNull(ownerName)){
    val creditCard = CreditCard(creditNumber,expireDate,cvv,ownerName)
    saveCreditCard(creditCard)
    //Todo remove this fragment
}else{
    Toasty.error(requireContext(),"Please Enter Valid Data",Toast.LENGTH_LONG)
}

        }

    }
    fun isTextEmptyOrNull(text:String):Boolean{
        if (text== null)
            return true
        if (text.isEmpty())
        return true
        return  false
    }
private fun isCreditNumberValid(number:String):Boolean{
    val regex = "^(?:(?<visa>4[0-9]{12}(?:[0-9]{3})?)|" +
            "(?<mastercard>5[1-5][0-9]{14})|" +
            "(?<discover>6(?:011|5[0-9]{2})[0-9]{12})|" +
            "(?<amex>3[47][0-9]{13})|" +
            "(?<diners>3(?:0[0-5]|[68][0-9])?[0-9]{11})|" +
            "(?<jcb>(?:2131|1800|35[0-9]{3})[0-9]{11}))$"
    val pattern: Pattern = Pattern.compile(regex)

    val matcher: Matcher = pattern.matcher(number)
   return  matcher.matches() && sumValidation(number)

}
    private fun sumValidation(number: String):Boolean{
        var sum = 0
        var alternate = false
        for (i in number.length - 1 downTo 0) {
            var n: Int = number.substring(i, i + 1).toInt()
            if (alternate) {
                n *= 2
                if (n > 9) {
                    n = n % 10 + 1
                }
            }
            sum += n
            alternate = !alternate
        }
        return sum % 10 == 0
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_credit, container, false)
    }
private fun saveCreditCard(creditCard: CreditCard){
    val gson = Gson()

    val mPrefs: SharedPreferences =
        requireContext().getSharedPreferences("test", Context.MODE_PRIVATE)
    val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
    val json = mPrefs.getString(Constants.sharedKey_Save_Credit_Card, "")

    val type = object : TypeToken<Set<Product>>(){}.type
    var productsInCart: HashSet<CreditCard>? = gson.fromJson(json, type)

    if (productsInCart != null){
        productsInCart.forEach { productInCart ->
            if (productInCart.carNumber == creditCard.carNumber){
                return
            }
        }
        productsInCart.add(creditCard)

    }else{
        productsInCart = HashSet<CreditCard>()
        productsInCart.add(creditCard)
    }
    val jsonToSave = gson.toJson(productsInCart)
    prefsEditor.putString(Constants.sharedKey_Add_Cart,jsonToSave)
    prefsEditor.commit()
}
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddCreditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCreditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}