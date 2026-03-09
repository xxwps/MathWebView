package com.example.mathwebview.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import com.agog.mathdisplay.MTFontManager
import com.agog.mathdisplay.MTMathView

/**
 * LaTeX 渲染工具类
 *
 * 基于 AndroidMath 库实现，提供将 LaTeX 公式转换为 Android 可用格式的能力。
 * 支持转换为 Bitmap、Drawable 或直接创建 View。
 *
 * ## 使用方式
 *
 * ```kotlin
 * // 1. 初始化（在 Application 或 Activity 中调用一次）
 * LaTeXRenderer.init(context)
 *
 * // 2. 渲染公式
 * val bitmap = LaTeXRenderer.renderToBitmap(
 *     latex = "E = mc^2",
 *     textSize = 24f,
 *     textColor = Color.BLACK
 * )
 * ```
 *
 * @see MTMathView AndroidMath 的核心视图组件
 */
object LaTeXRenderer {

    private var isInitialized = false
    private var appContext: Context? = null

    /**
     * 初始化 LaTeX 渲染引擎
     *
     * 必须在使用其他方法前调用。通常在 Application.onCreate() 或 Activity.onCreate() 中调用。
     * 多次调用是安全的，只有第一次调用会执行初始化。
     *
     * @param context Android Context，内部会使用 applicationContext
     * @return 初始化是否成功
     */
    fun init(context: Context): Boolean {
        return try {
            if (!isInitialized) {
                appContext = context.applicationContext
                MTFontManager.setContext(context.applicationContext)
                isInitialized = true
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 将 LaTeX 公式渲染为 Bitmap
     *
     * @param latex LaTeX 公式字符串
     * @param textSize 字体大小，单位为 sp
     * @param textColor 文字颜色，默认深灰色
     * @param maxWidth 最大宽度限制，单位为像素
     * @return 渲染后的 Bitmap，渲染失败返回 null
     * @throws IllegalStateException 如果未初始化
     */
    fun renderToBitmap(
        latex: String,
        textSize: Float = 20f,
        textColor: Int = Color.parseColor("#333333"),
        maxWidth: Int = 720
    ): Bitmap? {
        val ctx = appContext ?: return null
        check(isInitialized) { "LaTeXRenderer 未初始化，请先调用 init(context)" }

        return try {
            val mathView = MTMathView(ctx).apply {
                this.latex = latex
                this.fontSize = textSize
                this.textColor = textColor
            }

            mathView.measure(
                View.MeasureSpec.makeMeasureSpec(maxWidth, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            val width = mathView.measuredWidth.coerceAtLeast(1)
            val height = mathView.measuredHeight.coerceAtLeast(1)

            mathView.layout(0, 0, width, height)

            Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).also { bitmap ->
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.TRANSPARENT)
                mathView.draw(canvas)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 将 LaTeX 公式渲染为 Drawable
     *
     * 适用于需要在 ImageView 中显示公式的场景。
     *
     * @param context Context 用于创建 BitmapDrawable
     * @param latex LaTeX 公式字符串
     * @param textSize 字体大小
     * @param textColor 文字颜色
     * @return 渲染后的 Drawable，失败返回 null
     */
    fun renderToDrawable(
        context: Context,
        latex: String,
        textSize: Float = 20f,
        textColor: Int = Color.parseColor("#333333")
    ): Drawable? {
        val bitmap = renderToBitmap(latex, textSize, textColor) ?: return null
        return BitmapDrawable(context.resources, bitmap)
    }

    /**
     * 创建可直接添加到布局的 MTMathView 实例
     *
     * @param context Context
     * @param latex LaTeX 公式字符串
     * @param textSize 字体大小
     * @return 配置好的 MTMathView 实例
     */
    fun createMathView(
        context: Context,
        latex: String,
        textSize: Float = 20f
    ): MTMathView {
        return MTMathView(context).apply {
            this.latex = latex
            this.fontSize = textSize
        }
    }

    /**
     * 批量渲染多个公式
     *
     * 适用于需要一次性渲染多个公式的场景，如列表显示。
     *
     * @param latexList 公式列表
     * @param textSize 字体大小
     * @param textColor 文字颜色
     * @return Map，key 为原公式字符串，value 为渲染的 Bitmap（可能为 null）
     */
    fun renderBatch(
        latexList: List<String>,
        textSize: Float = 20f,
        textColor: Int = Color.parseColor("#333333")
    ): Map<String, Bitmap?> {
        return latexList.associateWith { latex ->
            renderToBitmap(latex, textSize, textColor)
        }
    }

    /**
     * 检查渲染器是否已初始化
     */
    fun isReady(): Boolean = isInitialized
}
