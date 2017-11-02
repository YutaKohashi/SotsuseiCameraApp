package jp.yuta.kohashi.sotsuseicameraapp.netowork.api

import io.reactivex.Observable
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model.Model
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 17 / 10 / 2017
 */
interface SotsuseiJsonApiService {

    @GET("")
    fun postImage(@Body fileName: String): Observable<Model.Result>

    @Multipart
    @POST("/api/images/")
    fun postImage(@Part image:MultipartBody.Part, @Part("imageId") storeId:RequestBody): Observable<Model.Result>

    @Multipart
    @POST("/")
    fun postImage(@Part image:MultipartBody.Part): Observable<Model.Result>

    companion object    {

        private val BASE_URL = "http://172.22.124.149:8000"

        fun create(): SotsuseiJsonApiService {

            val interceptor = Interceptor { chain ->
                chain.proceed(chain.request().newBuilder()
                        .header("Accept", "*/*")
//                        .header("Content-Type","application/json")
                        .method(chain.request().method(), chain.request().body())
                        .build())
            }

            val client = OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
                addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            }.build()

            return Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(client)
                    .build()
                    .create(SotsuseiJsonApiService::class.java)
        }
    }
}