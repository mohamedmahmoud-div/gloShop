package com.treecode.GloShop.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.login.ChangePasswordRequest
import com.treecode.GloShop.data.model.login.ChangePasswordResponse
import com.treecode.GloShop.data.model.login.DataBody
import com.treecode.GloShop.ui.main.registration.BaseRegisterActivity
import com.example.mvvmcoorutines.data.api.ApiService
import com.google.gson.Gson
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var apiInterface: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)

        val baseUrl = "http://161.35.113.112:8024/password-reset/confirm/"
        val intent = intent
        if (Intent.ACTION_VIEW == intent.action) {
            val uri: Uri? = intent.data
        val stringUrl = uri.toString()
         val substring=   stringUrl.substringAfter(baseUrl)
            val uui = substring.substringBefore("/")
            val token = substring.substringAfter("/").substringBefore("/")

            btn_save_new_password.setOnClickListener {
                val textNewPassword = edit_new_password.text.toString()
                val textConfirmPassword = edit_new_password_confirm.text.toString()
                if (textNewPassword.length < 7){
                    Toasty.error(this,getString(R.string.invalid_password),Toasty.LENGTH_LONG,true).show()
                    return@setOnClickListener
                }else if (textConfirmPassword != textNewPassword){
                    Toasty.error(this,getString(R.string.invalid_confirm_password),Toasty.LENGTH_LONG,true).show()
                    return@setOnClickListener
                }
                val changePasswordRequest = ChangePasswordRequest(uui,token,textNewPassword,textConfirmPassword)
                val callResetPassword = apiInterface.passwordReset(changePasswordRequest)
                callResetPassword.enqueue(object : Callback<ChangePasswordResponse>{
                    @SuppressLint("StringFormatInvalid")
                    override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                        Toasty.error(this@ChangePasswordActivity,getString(R.string.please_check_internet_connection),Toasty.LENGTH_LONG,true).show()

                        call.cancel()
                    }

                    override fun onResponse(
                        call: Call<ChangePasswordResponse>,
                        response: Response<ChangePasswordResponse>
                    ) {
                        if(response.code() ==400){
                            val jObjError = response.errorBody()!!.string()
                    val gson = Gson()
                            val erroBody:DataBody= gson.fromJson(jObjError,DataBody::class.java)
                            val errors = erroBody.error
                            if (!errors.isNullOrEmpty()){
                                Toasty.error(this@ChangePasswordActivity,errors[0],Toasty.LENGTH_LONG,true).show()
                            }
                        return
                        }
                        if (response.code() == 200){
                            val changePasswordRespone = response.body()
                            if (changePasswordRespone?.error.isNullOrEmpty()){
                                Toasty.success(this@ChangePasswordActivity,changePasswordRespone?.message!!,Toasty.LENGTH_LONG).show()
                                navigateToLoginScreen()
                            }
                        }

                    }

                })
            }
        }
    }
    private fun navigateToLoginScreen(){
        val intent = Intent(this, BaseRegisterActivity::class.java)
        startActivity(intent)
        finish()
    }
}