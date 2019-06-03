package com.example.mygithubrequest

import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient {

    companion object {

        fun getClient(): Retrofit {
            val okHttpClient = OkHttpClient.Builder().addNetworkInterceptor(StethoInterceptor()).build()
            Log.i("ApiClient", "url = ${BuildConfig.HOST}")
            return Retrofit.Builder()
                .baseUrl(BuildConfig.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }


        fun getHeaderMap() = mapOf("Authorization" to BuildConfig.AUTH_TOKEN, "accept" to "application/json")
    }

}