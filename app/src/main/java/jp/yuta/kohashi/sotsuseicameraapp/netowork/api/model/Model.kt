package jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 17 / 10 / 2017
 */
object Model {
    data class TokenResponse(val token:String)
    data class DefaultResponse(val res:String)
    data class ImgResponse(val res: String)
    data class StoreInfo(val sid: String, val gid: String, val sname: String, val ownername: String, val address: String, val latitude: Float, val longitude: Float)
    data class LoginResponse(val token:String)
//    data class Result(val query: Query)
//    data class Query(val searchinfo: SearchInfo)
//    data class SearchInfo(val totalhits: Int)
}