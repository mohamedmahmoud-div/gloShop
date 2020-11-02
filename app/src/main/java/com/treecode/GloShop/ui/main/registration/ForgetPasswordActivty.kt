package com.treecode.GloShop.ui.main.registration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.login.DataBody
import com.example.mvvmcoorutines.data.api.ApiService
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_forget_password_activty.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivty : AppCompatActivity() {
    private lateinit var apiInterface: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password_activty)
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
edit_email_forget.addTextChangedListener(object:TextWatcher {
    override fun afterTextChanged(p0: Editable?) {
        text_error_email_forget.visibility = View.GONE
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
})
        btn_send_to_email.setOnClickListener {
            text_error_email_forget.visibility = View.GONE

            val email = edit_email_forget.text.toString()
            if (email.isNullOrEmpty() || !email.contains("@") ||  !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                text_error_email_forget.text = getString(R.string.invalid_email)
                text_error_email_forget.visibility = View.VISIBLE
            } else {
                val senTOEmailCall = apiInterface.sendToMyEmail(email)
                loading_email.visibility = View.VISIBLE
                senTOEmailCall.enqueue(object: Callback<DataBody> {
                    override fun onFailure(call: Call<DataBody>, t: Throwable) {
                        Toasty.error(this@ForgetPasswordActivty,getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG,true).show()
                        loading_email.visibility = View.GONE

                        call.cancel()
                    }

                    override fun onResponse(call: Call<DataBody>, response: Response<DataBody>) {
                        val dataBody = response.body()
                        loading_email.visibility = View.GONE

                        text_error_email_forget.visibility = View.VISIBLE
                        if (dataBody != null)
                        text_error_email_forget.text = dataBody.message
                    }

                })
            }
        }
    }
}