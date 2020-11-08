package com.treecode.GloShop.data.api

import com.example.mvvmcoorutines.data.api.ApiService
import com.treecode.GloShop.ui.main.BaseActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


object RetrofitBuilder {

     const val BASE_URL = "http://gloshop.tree-code.com"

     fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC).setLevel(HttpLoggingInterceptor.Level.BODY).setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client =
            OkHttpClient.Builder().addInterceptor(interceptor).build()
//         val langauge =  Locale.getDefault().getDisplayLanguage();
//         var languageHeadeValue = "en"
//if(langauge == "English"){
//languageHeadeValue = "en"
//}else if (langauge == "العربية") {
//    languageHeadeValue = "ar"
//
//}
        val client2: OkHttpClient =
            OkHttpClient.Builder().addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    var langauge =  Locale.getDefault().language
                    //  var languageHeadeValue = "en"

                    if (BaseActivity.dLocale != null){
                        if (!BaseActivity.dLocale!!.language.isNullOrEmpty()){
                            langauge = BaseActivity.dLocale!!.language
                            // languageHeadeValue = langauge

                        }
                    }
                   // val sessionManger = SessionManager()
                    val newRequest: Request = chain.request().newBuilder()
                      //  .addHeader("Authorization", "Bearer ca76dc02d1d5401a584545fb8714f5e87fa52156http://161.35.113.112:8024")
                        .addHeader("Accept-Language", langauge)

                        .build()
                    return chain.proceed(newRequest)
                }
            }).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client2)
            .build() //Doesn't require the adapter
    }

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}