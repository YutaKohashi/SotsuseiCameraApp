package jp.yuta.kohashi.sotsuseicameraapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
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

    /**
     *  Keep screen on flag
     */
    open protected val KEEP_SCREEN_ON = false


    protected var mRootView: ViewGroup? = null
    protected var isEvent: Boolean = true

    open protected val FULL_SCREEN: Boolean = false

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreate(savedInstanceState, true)
    }

    open fun onCreate(savedInstanceState: Bundle?, isEvent: Boolean) {
        super.onCreate(savedInstanceState)

        contentViewFromRes?.let { setContentView(it) }
        contentViewFromView?.let { setContentView(it) }
        fragment?.let {
            mRootView = FrameLayout(this).apply {
                id = View.generateViewId()
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
            setContentView(mRootView)
            supportFragmentManager.beginTransaction().apply { add(mRootView!!.id, it) }.commit()
        }
        if (isEvent) setEvent()
    }

    open fun replaceFragment(fragment: Fragment) {
        if (mRootView == null) return
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(mRootView!!.id, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    open fun addFragment(fragment: Fragment) {
        if (mRootView == null) return
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(mRootView!!.id, fragment)
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()
        if (KEEP_SCREEN_ON) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (FULL_SCREEN) fullScreen()
    }

    override fun onPause() {
        super.onPause()
        if (KEEP_SCREEN_ON) window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /**
     * 戻るボタンの押下イベント
     */
    override fun onBackPressed() {
        super.onBackPressed()
        currentFragment()?.let {
            /**
             * fragment側でfalseを返すとactivityのonbackpressedイベントは呼ばれない
             */
            if ((it as? BaseFragment)?.onBackPressed() == false) return
            else onBackPressed()
        } ?: super.onBackPressed()
    }

    protected fun currentFragment(): Fragment? {
        try {
            return supportFragmentManager.fragments?.filter { it != null && it.isVisible }?.get(0)
        } catch (e: Exception) {
            return null
        }
    }

    /**
     * ボタンのクリック処理などを中心に記述
     */
    abstract fun setEvent()

    private fun fullScreen() {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN
    }
}
