package jp.yuta.kohashi.sotsuseicameraapp.netowork.api

import android.telecom.Call
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model.SampleModel
import retrofit2.Callback
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 10 / 10 / 2017
 */

class SotsuseiJsonApi() {
    private val mService: SotsuseiJsonApi.service

    init {
        mService = ApiClient.client.create<SotsuseiJsonApi.service>(SotsuseiJsonApi.service::class.java)
    }


    fun getResult(query:String,callback: Callback<SampleModel>){
        mService.getResult(query).enqueue(callback)
    }

    interface service {
        @POST("/◯◯◯◯◯/◯◯◯◯/")
        fun getResult(@Query("format") format: String): Call
    }
}