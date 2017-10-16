package jp.yuta.kohashi.sotsuseicameraapp.netowork.api

import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 11 / 10 / 2017
 */
object ApiClient {
    private val retrofit: Retrofit? = null

    val client: Retrofit
        get() {
            if (retrofit == null) {

                val builder = Retrofit.Builder()
                /**
                 * TODO BASE URL
                 */
                builder.baseUrl("")
                builder.addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                return builder.build()
            }
            return retrofit
        }
}