package com.example.mathwebview.data.model

import androidx.annotation.StringRes
import com.example.mathwebview.R

/**
 * LaTeX 渲染模式密封类
 * 使用密封类确保类型安全，并提供每种模式的相关信息
 *
 * @property titleResId 模式标题资源 ID
 * @property descriptionResId 模式描述资源 ID
 * @property tabIndex Tab 索引位置
 */
sealed class RenderMode(
    @StringRes val titleResId: Int,
    @StringRes val descriptionResId: Int,
    val tabIndex: Int
) {
    /**
     * WebView + MathJax 渲染模式
     * 使用 WebView 加载 MathJax JavaScript 库进行渲染
     * 优点：LaTeX 支持最完整
     * 缺点：需要网络（首次加载），渲染较慢，内存占用高
     */
    data object WebView : RenderMode(
        titleResId = R.string.mode_webview,
        descriptionResId = R.string.mode_webview_info,
        tabIndex = 0
    )
    
    /**
     * AndroidMath 原生渲染模式
     * 使用纯 Kotlin 实现的 AndroidMath 库，将 LaTeX 渲染为 Bitmap/View
     * 优点：无需网络，渲染快，内存占用低
     * 缺点：LaTeX 支持相对有限
     */
    data object Canvas : RenderMode(
        titleResId = R.string.mode_android_math,
        descriptionResId = R.string.mode_android_math_info,
        tabIndex = 1
    )
    
    /**
     * Compose LaTeX Renderer 渲染模式
     * 使用 Jetpack Compose 的 latex-renderer 库
     * 优点：跨平台，支持 372+ LaTeX 特性，与 Compose 完美集成
     * 缺点：依赖较大
     */
    data object Compose : RenderMode(
        titleResId = R.string.mode_compose,
        descriptionResId = R.string.mode_compose_info,
        tabIndex = 2
    )
    
    companion object {
        /** 默认渲染模式 */
        val DEFAULT = WebView
        
        /** 根据 Tab 索引获取对应的渲染模式 */
        fun fromTabIndex(index: Int): RenderMode = when (index) {
            0 -> WebView
            1 -> Canvas
            2 -> Compose
            else -> DEFAULT
        }
        
        /** 所有渲染模式列表 */
        val entries: List<RenderMode> = listOf(WebView, Canvas, Compose)
    }
}
