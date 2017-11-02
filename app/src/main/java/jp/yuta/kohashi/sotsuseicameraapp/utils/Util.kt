package jp.yuta.kohashi.sotsuseicameraapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import jp.yuta.kohashi.sotsuseicameraapp.App
import java.io.ByteArrayOutputStream

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 10 / 10 / 2017
 */

object Util {
//
//
//    /**
//     * ブロードキャストレシーバが登録されているか
//     * 引数:Intentクラスの定数を使用
//     */
//    fun registeredBroadcastReceiver(receiver:String):Boolean{
//        return false
//    }
//

    fun bmp2byte(bitmap:Bitmap):ByteArray{
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    /**
     * Glideを使用してDrawableリソースをimageviewに適用
     * @param id
     * @param targetImageView
     */
    fun setImageByGlide(@DrawableRes id: Int, targetImageView: ImageView) {
        Glide.with(App.context).load(id).into(targetImageView)
    }

    fun setImageByGlide(bmp: Bitmap, targetImageView: ImageView) {
        Glide.with(App.context).load(bmp).into(targetImageView)
    }

    fun layoutRes2View(context: Context, @LayoutRes layoutRes: Int, root: ViewGroup, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, root, attachToRoot)
    }

}