package jp.yuta.kohashi.sotsuseicameraapp.ui.view

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue

/**
 * Author : yutakohashi
 * Project name : SotsuseiClientApp
 * Date : 23 / 01 / 2018
 */

class AutoResizeTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    /**
     * テキスト幅計測用のペイント。
     */
    private val mPaint = Paint()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth.toFloat()
        if (measuredWidth > 0) {
            shrinkTextSize()
        }
    }

    /**
     * テキストサイズを縮小します。
     */
    private fun shrinkTextSize() {
        // テキストサイズを取得します。
        var tempTextSize = textSize
        // テキスト幅が入りきっていない場合は、入るまで繰り返します。
        while (measuredWidth < getTextWidth(tempTextSize)) {
            // テキストサイズを縮小します。
            tempTextSize--
            if (tempTextSize <= MIN_TEXT_SIZE) {
                // 最小テキストサイズより小さくなった場合は、最小テキストサイズをセットして終了します。
                tempTextSize = MIN_TEXT_SIZE
                break
            }
        }
        // 調整した結果のテキストサイズをセットします。
        setTextSize(TypedValue.COMPLEX_UNIT_PX, tempTextSize)
    }

    /**
     * テキスト幅を取得します。
     *
     * @param textSize
     * @return
     */
    internal fun getTextWidth(textSize: Float): Float {
        mPaint.textSize = textSize
        return mPaint.measureText(text.toString())
    }

    companion object {
        /**
         * 最小のテキストサイズ。
         */
        private val MIN_TEXT_SIZE = 6.0f
    }
}