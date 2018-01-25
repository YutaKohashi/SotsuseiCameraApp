package jp.yuta.kohashi.sotsuseicameraapp.netowork.api

import android.graphics.Bitmap
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.exception.ApiException
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model.Model
import jp.yuta.kohashi.sotsuseicameraapp.utils.Util
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 17 / 10 / 2017
 */
object SotsuseiApiHelper {
    private val TAG = SotsuseiApiHelper::class.java.simpleName

    private val mCompositeDisposable: CompositeDisposable = CompositeDisposable()

    val sotsuseiApiService by lazy {
        SotsuseiJsonApiService.create()
    }

    interface Callback<T> {
        fun onSuccess(body: T?)
        fun onFailure(e: ApiException)
    }

    // 画像アップロード
//    fun uploadImage(bmp:Bitmap,callback: (model: Model.Query?, error: Boolean, type: ApiException.ErrorType) -> Unit) {
//
//        disposable = sotsuseiApiService.postImage("").
//                subscribeOn(Schedulers.io()).
//                observeOn(AndroidSchedulers.mainThread()).
//                subscribe({ result ->
//                    val body: Model.Query = result.query
//                    callback.invoke(body, false, ApiException.ErrorType.ERROR_TYPE_UNKNOWN)
//                }, { error ->
//                    error.message
//                    callback.invoke(null, true, ApiException.ErrorType.ERROR_TYPE_API_STATUS)
//                })
//    }
    fun postStoreCreateToken(sid: String, pass: String, callback: Callback<Model.TokenResponse>) {
        val storeIdBody: RequestBody = RetroUtil.string2reqbody(sid)
        val passwordBody: RequestBody = RetroUtil.string2reqbody(pass)

        val disposable: Disposable = sotsuseiApiService.postStoreCreateToken(storeIdBody, passwordBody).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe({ body: Model.TokenResponse? ->
                    callback.onSuccess(body)
                }, { error ->
                    callback.onFailure(ApiException.error(error))
                }, {

                })
        mCompositeDisposable.add(disposable)
    }

    fun postStoreVerifyToken(sid: String, token: String, callback: Callback<Model.DefaultResponse>) {
        val sidBody: RequestBody = RetroUtil.string2reqbody(sid)
        val tokenBody: RequestBody = RetroUtil.string2reqbody(token)

        val disposable: Disposable = sotsuseiApiService.postStoreVerifyToken(sidBody, tokenBody).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe({ body: Model.DefaultResponse? ->
                    callback.onSuccess(body)
                }, { error ->
                    callback.onFailure(ApiException.error(error))
                }, {

                })
        mCompositeDisposable.add(disposable)
    }

    fun postRevocationToken(token: String, callback: Callback<Model.DefaultResponse>) {
        val tokenBody: RequestBody = RetroUtil.string2reqbody(token)

        val disposable: Disposable = sotsuseiApiService.postRevocationToken(tokenBody).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe({ body: Model.DefaultResponse? ->
                    callback.onSuccess(body)
                }, { error ->
                    callback.onFailure(ApiException.error(error))
                }, {

                })
        mCompositeDisposable.add(disposable)
    }


    // 画像アップロード
    fun uploadImage(bmp: Bitmap, storeId: String, callback: (res: Model.ImgResponse?, type: ApiException.ErrorType) -> Unit) {

        val imageBody: MultipartBody.Part = RetroUtil.bmp2Part(bmp, "imageData", "fileName")
        val storeIdBody: RequestBody = RetroUtil.string2reqbody(storeId)

        val disposable: Disposable = sotsuseiApiService.postImage(imageBody, storeIdBody).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe({ result: Model.ImgResponse? ->
                    val body = result
                    callback.invoke(body, ApiException.ErrorType.ERROR_NO)
                }, { error ->
                    callback.invoke(null, ApiException.ErrorType.ERROR_TYPE_API_STATUS)
                })
        mCompositeDisposable.add(disposable)
    }

    fun getStoreInfo(storeId: String, callback: (res: Model.StoreInfo?, type: ApiException.ErrorType) -> Unit) {
        val disposable: Disposable = sotsuseiApiService.getStoreInfo(storeId).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe({ result: Model.StoreInfo? ->
                    val body = result
                    callback.invoke(body, ApiException.ErrorType.ERROR_NO)
                }, { error ->
                    callback.invoke(null, ApiException.ErrorType.ERROR_TYPE_API_STATUS)
                })
        mCompositeDisposable.add(disposable)
    }


    fun dispose() {
//        if (disposable != null && !disposable!!.isDisposed) disposable?.dispose()
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.clear()
            Log.d(TAG, "dispose in SotsuseiApiHelper")
        }
    }


    // region private methods


    private object RetroUtil {
        fun bmp2Part(bmp: Bitmap, name: String, fileName: String): MultipartBody.Part {
            return reqbody2part(bmp2reqbody(bmp), name, fileName)
        }

        fun reqbody2part(requestBody: RequestBody, name: String, fileName: String): MultipartBody.Part {
            return MultipartBody.Part.createFormData(name, "fileName.png", requestBody)
        }

        fun bmp2reqbody(bitmap: Bitmap): RequestBody {
            return RequestBody.create(MediaType.parse("image/png"), Util.bmp2byte(bitmap))
        }

        fun string2reqbody(string: String): RequestBody {
            return RequestBody.create(MediaType.parse("multipart/form-data"), string)
        }
    }

    // endregion

}