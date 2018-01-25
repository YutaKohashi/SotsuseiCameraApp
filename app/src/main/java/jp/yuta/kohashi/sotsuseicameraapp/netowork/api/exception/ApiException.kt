package jp.yuta.kohashi.sotsuseicameraapp.netowork.api.exception

import java.io.IOException

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 26 / 10 / 2017
 */
class ApiException(errorType: ErrorType) : IOException() {

    lateinit var errorType: ErrorType
    lateinit var apiStatus: ApiStatusType

    init {
//        this(errorType, null, ApiResponse.STATUS_OK)
    }

    companion object {
        fun error(e:Throwable):ApiException{
            return ApiException(ErrorType.ERROR_TYPE_API_STATUS)
        }
    }

    constructor(errorType: ErrorType, errorMessage: String) : this(errorType) {
//        this(errorType,errorMessage)
    }

    constructor(errorType: ErrorType, errorMessage: String, apiStatus: ApiStatusType) : this(errorType, errorMessage) {
        this.errorType = errorType
        this.apiStatus = apiStatus
    }

    private fun defaultMessage(errorType: ErrorType): String {
        var msg: String? = null

        when (errorType) {
            ErrorType.ERROR_TYPE_UNKNOWN -> {
            }
            ErrorType.ERROR_TYPE_API_STATUS -> {
            }
            ErrorType.ERROR_TYPE_HTTP_STATUS -> {
            }
            ErrorType.ERROR_TYPE_JSON_PARSE -> {
            }
        }

        return msg ?: ""
    }

    enum class ErrorType(num: Int) {
        ERROR_NO(0),
        ERROR_TYPE_UNKNOWN(0),
        ERROR_TYPE_API_STATUS(1),
        ERROR_TYPE_HTTP_STATUS(2),
        ERROR_TYPE_JSON_PARSE(3)
    }

    enum class ApiStatusType(num: Int) {
        API_STATUS_OK(0),
        API_STATUS_ERROR_SYSTEM(1),
        API_STATUS_ERROR_AUTH(2),
        API_STATUS_ERROR_PERMISSION(3),
        API_STATUS_ERROR_OPENREC_WITHDRAWAL(4),

        API_STATUS_ERROR_PROFILE_SETTING_NAME(5),
        API_STATUS_ERROR_PROFILE_SETTING_KEY(6),
        API_STATUS_ERROR_PROFILE_SETTING_KEY_OVERLAP(7)
    }
}