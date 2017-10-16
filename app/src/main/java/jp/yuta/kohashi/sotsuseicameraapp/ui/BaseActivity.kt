package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * Activityを作る際は必ず継承してください
 */
abstract class BaseActivity : AppCompatActivity() {

    /**
     * フラグメントを設置するとき
     */
    open protected val fragment: Fragment? = null

    /**
     * レイアウトリソースを設置するとき
     */
    @LayoutRes
    open protected val contentViewFromRes: Int? = null

    /**
     * Viewを設置するとき
     */
    open protected val contentViewFromView: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contentViewFromRes?.let { setContentView(it) }
        contentViewFromView?.let { setContentView(it) }
        fragment?.let {
            val rootView = FrameLayout(this).apply {
                id = View.generateViewId()
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
            setContentView(rootView)
            supportFragmentManager.beginTransaction().apply { add(rootView.id, it) }.commit()
        }
        setEvent()
    }

    /**
     * ボタンのクリック処理などを中心に記述
     */
    abstract fun setEvent()

}
