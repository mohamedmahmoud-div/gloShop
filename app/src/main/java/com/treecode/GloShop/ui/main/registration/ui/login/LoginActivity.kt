package com.treecode.GloShop.ui.main.registration.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.login.LoginResponse
import com.treecode.GloShop.ui.main.home.ui.main.HomeActivity
import com.treecode.GloShop.ui.viewmodel.ViewModelFactory
import com.example.mvvmcoorutines.data.api.ApiHelper
import com.example.mvvmcoorutines.data.api.ApiService
import es.dmoral.toasty.Toasty
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var apiInterface: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(
            R
            .layout.activity_login)
       apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

//        val toolbar = login_toolbar
//        setSupportActionBar(toolbar)
//        toolbar?.setTitle("Toolbar")
//        toolbar?.setSubtitle("Subtitle")
//        toolbar?.navigationIcon = ContextCompat.
//        getDrawable(this,R.drawable.ic_search)

        loginViewModel = ViewModelProviders.of(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService)))
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                     //   loginViewModel.login(
//                 //           username.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }

            login.setOnClickListener {


                /**
                 * POST name and job Url encoded.
                 */
                val call3 =
                    apiInterface.loginTest(username.text.toString(), password.text.toString())

                call3.enqueue(object : Callback<LoginResponse?> {
                    override fun onResponse(
                        call: retrofit2.Call<LoginResponse?>,
                        response: Response<LoginResponse?>
                    ) {
                       val erroBody =  response.errorBody().toString()
                        val header = response.headers()
                        val loginResponse: LoginResponse? = response.body()
                        val text: String? = loginResponse?.message
if (text != null && text == ""){
    showResponse(loginResponse)
} else if (text != null ){
    Toasty.error(applicationContext, "$text page", Toast.LENGTH_LONG, true).show();
}

                    }

                    override fun onFailure(
                        call: retrofit2.Call<LoginResponse?>,
                        t: Throwable
                    ) {
                        call.cancel()

                    }
                })








//                loginViewModel.login(username.text.toString(),password.text.toString()).observe( this@LoginActivity,
//                    Observer {
//                        it?.let { resource ->
//                            when (resource.status) {
//                                Status.SUCCESS -> {
//                                    loading.visibility = View.GONE
//
//                                    resource.data?.let { loginResponse ->
//                                        showResponse(loginResponse)
//
//
//                                    }
//
//                                }
//                                Status.ERROR -> {
//
//
//                                    resource.data?.let { it1 -> it1.message?.let { it2 ->
//                                        Log.e("Error",
//                                            it2
//                                        )
//                                    } }
//                                    loading.visibility = View.GONE
//                                }
//                                Status.LOADING -> {
//                                    it?.message?.let { it1 -> Log.e("Error", it1) }
//                                    loading.visibility = View.VISIBLE
//
//                                    ///   progressBar.visibility = View.VISIBLE
//                                    //  recyclerView.visibility = View.GONE
//
//                                }
//                            }
//                        }
//                    })




//val user = UserLoginRequest(username.text.toString(),password.text.toString())
//                val call  = apiInterface.loginUser(user)
                //loginViewModel.login(username.text.toString(),password.text.toString()).o
                //loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun showResponse(loginResponse: LoginResponse){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    private fun setupViewModel(){
        loginViewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(LoginViewModel::class.java)
    }
private fun setupObserver(){

}


    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}