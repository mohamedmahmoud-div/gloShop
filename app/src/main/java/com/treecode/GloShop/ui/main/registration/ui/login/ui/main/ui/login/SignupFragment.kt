package com.treecode.GloShop.ui.main.registration.ui.login.ui.main.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.treecode.GloShop.R
import com.treecode.GloShop.data.api.RetrofitBuilder
import com.treecode.GloShop.data.model.login.LoginResponse
import com.treecode.GloShop.data.model.login.SignupRequest
import com.treecode.GloShop.data.model.login.SocialCallBackData
import com.treecode.GloShop.data.model.login.SocialSignUpRequest
import com.treecode.GloShop.ui.main.home.ui.main.HomeActivity
import com.treecode.GloShop.ui.main.registration.ui.login.ui.main.register.SignupViewModel
import com.treecode.GloShop.util.Constants
import com.treecode.GloShop.util.SessionManager
import com.example.mvvmcoorutines.data.api.ApiService
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.treecode.GloShop.util.UserAccountManger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_signup.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var fullNamedEditText: EditText
    private lateinit var userNameEditText: EditText

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var signupViewModel: SignupViewModel
    private lateinit var apiInterface: ApiService
    lateinit var callbackManager: CallbackManager
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val EMAIL = "email"

    var genderSelectd = 1
    var isSoicalRegister = false
    var faceBookID:String? = null
    var gmailID:String? = null
    private val GMAIL_SIGN_IN = 9001
    private var socialCallBackData:SocialCallBackData? = null
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
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signupViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(SignupViewModel::class.java)
        apiInterface = RetrofitBuilder.getRetrofit().create(ApiService::class.java)
         emailEditText = view.findViewById<EditText>(R.id.edit_sign_up_email)
         passwordEditText = view.findViewById<EditText>(R.id.edit_sign_up_password)
     confirmPasswordEditText = view.findViewById<EditText>(R.id.edit_confirm_password)
        fullNamedEditText = view.findViewById<EditText>(R.id.edit_full_name)
         userNameEditText = view.findViewById<EditText>(R.id.edit_user_name)
        val birthDatePicker = view.findViewById<DatePicker>(R.id.datePicker1)

        setupGoogle()

        val c: Calendar = Calendar.getInstance()
        c.set(2000, 2, 15)
        birthDatePicker.maxDate = c.timeInMillis
        val signUpButton = view.findViewById<Button>(R.id.btn_sign_up)
        radio_group_gender.check(R.id.radio_male)



radio_male.setOnClickListener{
    genderSelectd = 1
}
        radio_female.setOnClickListener{
            genderSelectd = 2
    }
        signupViewModel.loginFormState.observe(viewLifecycleOwner,
            Observer { loginFormState ->
                if (loginFormState == null) {
                    return@Observer
                }
                signUpButton.isEnabled = loginFormState.isDataValid
                loginFormState.usernameError?.let {
                    userNameEditText.error = getString(it)
                }
                loginFormState.passwordError?.let {
                    if(!isSoicalRegister)
                    passwordEditText.error = getString(it)
                    else signUpButton.isEnabled = true
                }
                loginFormState.confirmPasswordError?.let {
                    if(!isSoicalRegister)
                    confirmPasswordEditText.error = getString(it)
                    else signUpButton.isEnabled = true
                }
                loginFormState.emailError?.let {
                    emailEditText.error = getString(it)
                }
                loginFormState.dateError?.let {
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
                signupViewModel.loginDataChanged(
                    userNameEditText.text.toString(),
                    passwordEditText.text.toString(),
                    confirmPasswordEditText.text.toString(), fullNamedEditText.text.toString(),
                    emailEditText.text.toString()

                )
            }
        }

        emailEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        confirmPasswordEditText.addTextChangedListener(afterTextChangedListener)
        userNameEditText.addTextChangedListener(afterTextChangedListener)
        fullNamedEditText.addTextChangedListener(afterTextChangedListener)



        signUpButton.setOnClickListener {

            val dateText = birthDatePicker.year.toString() + "-"+ checkDigit(birthDatePicker.month + 1) + "-" + checkDigit(birthDatePicker.dayOfMonth)
            if(!isSoicalRegister)
            regularEmailSignUp(dateText)
            else
            socialSignUp(dateText)
        }


        callbackManager = CallbackManager.Factory.create();
        btn_sign_up_fb.setPermissions(EMAIL,"public_profile")

        btn_sign_up_fb.setFragment(this)
        btn_sign_up_fb.registerCallback(callbackManager,object : FacebookCallback<LoginResult> {
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
                                isSoicalRegister = true
                                faceBookID = id
                                socialCallBackData = SocialCallBackData(email=email,fullName = name,id = id)
                                refillInputsDataFromCallBack(email,name)
                            }

                        }
                    })
                val parameters = Bundle()
                parameters.putString("fields", "id,name,email,link")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
            }

            override fun onError(error: FacebookException?) {

            }

        })

        val accessToken = AccessToken.getCurrentAccessToken()
        accessToken != null && !accessToken.isExpired

        btn_sign_up_gmail.setOnClickListener {
            gmailSignIn()
        }
    }
    private fun setupGoogle(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Constants.GoogleID)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

    }
    private fun gmailSignIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, GMAIL_SIGN_IN
        )
    }
private fun refillInputsDataFromCallBack(email:String,fullName:String){
    passwordEditText.visibility = View.GONE
    confirmPasswordEditText.visibility = View.GONE
    emailEditText.setText(email)
    fullNamedEditText.setText(fullName)
    btn_sign_up_gmail.visibility  = View.GONE
    image_gmail.visibility = View.GONE
    image_facebook.visibility = View.GONE
    btn_sign_up_fb.visibility  = View.GONE
    btn_sign_up.text = getString(R.string.complete_sign_up)
}
    private fun regularEmailSignUp(date:String){
        val signupRequest = SignupRequest(userNameEditText.text.toString(),emailEditText.text.toString(),genderSelectd,date,passwordEditText.text.toString(),confirmPasswordEditText.text.toString(),fullNamedEditText.text.toString())


        // Regular Email Sign up
        val signupCallRequest =
            apiInterface.signup(signupRequest)
        signupCallRequest.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {

                handleSignUpResponse(response)
            }

            override fun onFailure(
                call: retrofit2.Call<LoginResponse?>,
                t: Throwable
            ) {
                call.cancel()

            }
        })
    }
    private fun socialSignUp(date:String){
        if (socialCallBackData != null ){
            val email = socialCallBackData!!.email
            val fullName = socialCallBackData!!.fullName
            val userName = userNameEditText.text.toString()
            if (userName.isNullOrEmpty()){
                userNameEditText.error = getString(R.string.invalid_username)
                Toasty.error(requireContext(),getString(R.string.invalid_username),Toasty.LENGTH_LONG,true).show()
                return
            }
            var socialSignUpRequest:SocialSignUpRequest
            if(faceBookID != null){
            socialSignUpRequest = SocialSignUpRequest(userName,email,genderSelectd,date,fullName = fullName,faceBookID = faceBookID,googleID = null)
            }else if(gmailID != null){
                socialSignUpRequest = SocialSignUpRequest(userName,email,genderSelectd,date,fullName = fullName,faceBookID = null,googleID = gmailID)

            } else return
            val socialSignUpCall = apiInterface.socialSignUp(socialSignUpRequest)
            socialSignUpCall.enqueue(object :Callback<LoginResponse> {
                @SuppressLint("StringFormatInvalid")
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toasty.error(requireContext(),getString(R.string.please_check_internet_connection,Toasty.LENGTH_LONG)).show()
                    call.cancel()

                }

                override fun onResponse(
                    call: Call<LoginResponse?>,
                    response: Response<LoginResponse?>
                ) {
                    handleSignUpResponse(response)

                }

            })
        }

    }
    private fun handleSignUpResponse(response: Response<LoginResponse?>){
        val header = response.headers()
        val loginResponse: LoginResponse? = response.body()
        val errors = loginResponse?.error
        val text: String? = loginResponse?.message
        if (text != null && text == ""){
            showResponse(loginResponse)
        } else if (!errors.isNullOrEmpty()){

            Toasty.error(requireContext(), errors[0], Toast.LENGTH_LONG, true).show();
        }
    }
    private fun checkDigit(number: Int): String? {
        if (number == 0 ){
        return "01"
        }
        return if (number <= 9) "0$number" else number.toString()
    }
    @SuppressLint("ShowToast")
    private fun showResponse(loginResponse: LoginResponse){
        val sessionManager = SessionManager(requireContext())
        loginResponse.data?.token?.let { sessionManager.saveAuthToken(it) }
        val user = loginResponse.data!!
        val userAccountManger = UserAccountManger(requireContext())
        userAccountManger.saveUser(user)

        val intent = Intent(requireContext(), HomeActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            // Signed in successfully
            val googleId = account?.id ?: ""

            if (!googleId.isNullOrEmpty()){
            isSoicalRegister = true
            }


            val googleFirstName = account?.givenName ?: ""


            val googleLastName = account?.familyName ?: ""

            val googleEmail = account?.email ?: ""

            val googleProfilePicURL = account?.photoUrl.toString()

            val googleIdToken = account?.idToken ?: ""


            gmailID = googleId
            val googleFullName = "$googleFirstName $googleLastName"
            socialCallBackData = SocialCallBackData(email = googleEmail,fullName = googleFullName,id = googleId)
            refillInputsDataFromCallBack(googleEmail,googleFullName)

        } catch (e: ApiException) {

        }
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
            super.onActivityResult(requestCode, resultCode, data)
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}