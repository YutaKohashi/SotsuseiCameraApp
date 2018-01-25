package jp.yuta.kohashi.sotsuseicameraapp.ui.info

import android.app.ProgressDialog
import android.widget.Toast
import jp.yuta.kohashi.sotsuseicameraapp.R
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.SotsuseiApiHelper
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.exception.ApiException
import jp.yuta.kohashi.sotsuseicameraapp.netowork.api.model.Model
import jp.yuta.kohashi.sotsuseicameraapp.ui.BaseFragment
import jp.yuta.kohashi.sotsuseicameraapp.utils.PrefUtil
import kotlinx.android.synthetic.main.fragment_info.*

/**
 * Author : yutakohashi
 * Project name : SotsuseiCameraApp
 * Date : 15 / 01 / 2018
 */

class InfoTenpoFragment :BaseFragment(){

    override val sLayoutRes: Int
        get() = R.layout.fragment_info

    override fun setEvent() {

        closeButton.setOnClickListener { activity.finish() }

        val prog = ProgressDialog(activity)
        prog.setMessage("通信中...")
        prog.show()
        // 通信
        SotsuseiApiHelper.getStoreInfo(PrefUtil.storeId,{
            res: Model.StoreInfo?, type: ApiException.ErrorType ->
            prog.dismiss()
            if(type != ApiException.ErrorType.ERROR_NO || res == null) {
                Toast.makeText(activity,"店舗情報の取得に失敗しました。",Toast.LENGTH_SHORT).show()
                return@getStoreInfo
            }
            applyStoreData(res)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        SotsuseiApiHelper.dispose()
    }

    fun applyStoreData(storeInfo: Model.StoreInfo) {
        storeNameTextView2.text = storeInfo.sname
        nameTextView.text = storeInfo.ownername
        addressTextView.text = storeInfo.address
    }

}