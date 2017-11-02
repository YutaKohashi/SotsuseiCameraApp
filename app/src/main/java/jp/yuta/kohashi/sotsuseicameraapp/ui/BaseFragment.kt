package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 27 / 09 / 2017
 */
abstract class BaseFragment : Fragment() {

    abstract val sLayoutRes: Int
        @LayoutRes get

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(sLayoutRes, container, false)

    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
    }

    protected fun popBackStack() {
        if (activity != null && activity.supportFragmentManager.backStackEntryCount != 0)
            activity.supportFragmentManager.popBackStack()
    }

    /**
     * 戻るボタンの押下イベント
     * @return backKey event through to activity
     */
    open fun onBackPressed(): Boolean = false


    /**
     * ボタンのクリック処理を記述chrome
     */

    abstract fun setEvent()

    /**
     * imageviewのリソースを解放する
     */
    protected fun ImageView.release() {
        drawable?.let { (it as BitmapDrawable).bitmap?.recycle() }
        setImageDrawable(null)
        setImageBitmap(null)
    }

}