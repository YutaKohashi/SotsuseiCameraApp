package jp.yuta.kohashi.sotsuseicameraapp.netowork.api

import android.graphics.Bitmap
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.exception.ApiException
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model.Model

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 17 / 10 / 2017
 */
object SotsuseiApiManager {

    private var disposable: Disposable? = null

    val sotsuseiApiService by lazy {
        SotsuseiJsonApiService.create()
    }

    fun uploadImage(bmp:Bitmap,callback: (model: Model.Query?, error: Boolean, type: ApiException.ErrorType) -> Unit) {

        disposable = sotsuseiApiService.postImage("").
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe({ result ->
                    val body: Model.Query = result.query
                    callback.invoke(body, false, ApiException.ErrorType.ERROR_TYPE_UNKNOWN)
                }, { error ->
                    error.message
                    callback.invoke(null, true, ApiException.ErrorType.ERROR_TYPE_API_STATUS)
                })
    }


    fun dispose() = disposable?.dispose()

}