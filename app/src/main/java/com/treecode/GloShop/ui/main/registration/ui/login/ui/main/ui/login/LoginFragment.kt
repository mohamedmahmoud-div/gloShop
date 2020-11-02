package com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.login.LoginResponse
import com.treecode.GloShop.ui.main.home.ui.main.HomeActivity
import com.treecode.GloShop.ui.main.registration.ForgetPasswordActivty
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.Constants.Companion.GoogleID
import com.treecode.GloShop.util.SessionManager
import com.treecode.GloShop.util.UserAccountManger
import com.example.mvvmcoorutines.data.api.ApiService
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.loading
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var apiInterface: ApiService
    private lateinit var sessionManager: SessionManager
    private val EMAIL = "email"
    lateinit var callbackManager: CallbackManager
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val GMAIL_SIGN_IN = 9001

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)

        val usernameEditText = view.findViewById<EditText>(R.id.username)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val loginButton = view.findViewById<Button>(R.id.btn_login)
        val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GoogleID)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        loginViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                loginButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    usernameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    passwordEditText.error = getString(it)
                }
            })

        loginViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { loginResult ->
                loginResult ?: return@Observer
                loadingProgressBar.visibility = View.GONE
                loginResult.error?.let {
                    showLoginFailed(it)
                }
                loginResult.success?.let {
                   // updateUiWithUser(it)
                }
            })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
        }
        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }
            false
        }
        text_forget_password.setOnClickListener {
            naviigateToEmailAddress()
        }
        loginButton.setOnClickListener {
            loadingProgressBar.visibility = View.VISIBLE
            sessionManager = SessionManager(requireContext())
            val loginRequestCall =
                apiInterface.loginTest(usernameEditText.text.toString(), passwordEditText.text.toString())
            loginRequestCall.enqueue(object : Callback<LoginResponse?> {
                override fun onResponse(
                    call: retrofit2.Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    loadingProgressBar.visibility = View.GONE

                    val erroBody =  response.errorBody().toString()
                    val header = response.headers()
                    val loginResponse: LoginResponse? = response.body()
                    val text: String? = loginResponse?.message
                    if (text != null && text == ""){

                        showResponse(loginResponse)
                    } else if (text != null ){
                        Toasty.error(requireContext(), "$text page", Toast.LENGTH_LONG, true).show();
                    }

                }

                override fun onFailure(
                    call: retrofit2.Call<LoginResponse?>,
                    t: Throwable
                ) {
                    loadingProgressBar.visibility = View.GONE

                    call.cancel()

                }
            })
        }
        val faceBookLoginBtn = view.findViewById<LoginButton>(R.id.btn_login_fb)

        callbackManager = CallbackManager.Factory.create();
        faceBookLoginBtn.setPermissions(EMAIL,"public_profile","user_friends")

        faceBookLoginBtn.setFragment(this)
        faceBookLoginBtn.registerCallback(callbackManager,object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    val request: GraphRequest = GraphRequest.newMeRequest(
                        result!!.accessToken,
                        object : GraphRequest.GraphJSONObjectCallback {
                            override fun onCompleted(
                                `object`: JSONObject?,
                                response: GraphResponse?
                            ) {
                                // Application code
                                val userObject = `object`
                                val id = userObject?.get("id") as String?
                                val name = userObject?.get("name") as String
                                val email = userObject?.get("email") as String
                                if (!id.isNullOrEmpty()){
                                    loginFBSocial(id!!,Constants.FB_SOCIAL_TYPE)
                                }

                            }
                        })
                    val parameters = Bundle()
                    parameters.putString("fields", "id,name,email,link")
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Toasty.error(requireContext(), "Login Cancelled", Toast.LENGTH_LONG).show()
                }

                override fun onError(error: FacebookException?) {
                    Toasty.error(requireContext(), error!!.message!!, Toast.LENGTH_LONG).show()

                }

            })




btn_login_gmail.setOnClickListener {
    gmailSignIn()
}
    }
private fun naviigateToEmailAddress(){
val intent = Intent(activity,ForgetPasswordActivty::class.java)
    startActivity(intent)
}
    private fun loginFBSocial(socialID:String,socialType:String){
        loading.visibility = View.VISIBLE

        val socialLoginCall = apiInterface.socialLogin(socialID = socialID,socialType = socialType)
        socialLoginCall.enqueue(object :Callback<LoginResponse> {
            @SuppressLint("StringFormatInvalid")
            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loading.visibility = View.GONE

                Toasty.error(requireContext(),getString(R.string.please_check_internet_connection,Toasty.LENGTH_LONG)).show()

            }

            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                loading.visibility = View.GONE
                val loginResponse = response.body()
                val data = loginResponse?.data
                val text: String? = loginResponse?.message
                if (data != null){
                    showResponse(loginResponse)
                } else {
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
                    Toasty.error(requireContext(), "$text", Toast.LENGTH_LONG, true).show();
                }
            }

        })
    }


    private fun gmailSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, GMAIL_SIGN_IN
        )
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""

            if (!googleId.isNullOrEmpty()){
                loginFBSocial(googleId!!,Constants.GMAIL_SOCIAL_TYPE)
            }


            val googleFirstName = account?.givenName ?: ""

            val googleLastName = account?.familyName ?: ""

            val googleEmail = account?.email ?: ""


            val googleProfilePicURL = account?.photoUrl.toString()

            val googleIdToken = account?.idToken ?: ""

        } catch (e: ApiException) {

            Toasty.error(requireContext(),e.localizedMessage).show()
        }
    }
    @SuppressLint("ShowToast")
    private fun showResponse(loginResponse: LoginResponse){
        sessionManager = SessionManager(requireContext())

        loginResponse.data?.token?.let { sessionManager.saveAuthToken(it) }

        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        //todo save in userdeafult
        val user = loginResponse.data!!
        val userAccountManger = UserAccountManger(requireContext())
        userAccountManger.saveUser(user)

        activity?.finish()


    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
     override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
         if (requestCode == GMAIL_SIGN_IN) {
             val task =
                 GoogleSignIn.getSignedInAccountFromIntent(data)
             handleSignInResult(task)
         } else {
             callbackManager.onActivityResult(requestCode, resultCode, data)

             super.onActivityResult(requestCode, resultCode, data)
         }
    }

}