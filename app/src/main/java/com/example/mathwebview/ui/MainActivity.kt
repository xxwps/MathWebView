package com.example.mathwebview.ui

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature
import com.example.mathwebview.R
import com.example.mathwebview.data.model.RenderMode
import com.example.mathwebview.databinding.ActivityMainBinding
import com.example.mathwebview.ui.screen.ComposeLatexScreen
import com.google.android.material.tabs.TabLayout

/**
 * 主界面 Activity
 *
 * 展示三种不同的 LaTeX 渲染方式：
 * 1. WebView + MathJax
 * 2. AndroidMath 原生渲染
 * 3. Compose LaTeX Renderer
 *
 * 使用 ViewBinding 进行视图绑定，遵循现代 Android 开发最佳实践。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentMode: RenderMode = RenderMode.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setupStatusBar()
        setupViewBinding()
        setupWebView()
        setupComposeView()
        setupTabs()
        setupBackPressHandler()
    }

    /**
     * 配置状态栏样式
     * 根据当前主题自动调整状态栏图标颜色
     */
    private fun setupStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDarkMode()
        }
    }

    /**
     * 初始化 ViewBinding
     */
    private fun setupViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * 配置 WebView
     * 启用 JavaScript、DOM 存储，并支持深色模式
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = true
                displayZoomControls = false
                cacheMode = WebSettings.LOAD_DEFAULT
            }

            setupWebViewDarkMode()
            webViewClient = WebViewClient()
            loadUrl(MATH_FORMULAS_URL)
        }
    }

    /**
     * 配置 WebView 深色模式支持
     */
    private fun setupWebViewDarkMode() {
        val webView = binding.webView
        
        if (WebViewFeature.isFeatureSupported(WebViewFeature.ALGORITHMIC_DARKENING)) {
            WebSettingsCompat.setAlgorithmicDarkeningAllowed(webView.settings, true)
        }

        if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
            @Suppress("DEPRECATION")
            WebSettingsCompat.setForceDark(
                webView.settings,
                if (isDarkMode()) WebSettingsCompat.FORCE_DARK_ON else WebSettingsCompat.FORCE_DARK_OFF
            )
        }
    }

    /**
     * 配置 ComposeView
     */
    private fun setupComposeView() {
        binding.composeView.setContent {
            ComposeLatexScreen()
        }
    }

    /**
     * 配置 Tab 切换监听
     */
    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.position?.let { position ->
                    switchToMode(RenderMode.fromTabIndex(position))
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    /**
     * 配置返回键处理
     * WebView 模式下支持页面后退
     */
    private fun setupBackPressHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currentMode == RenderMode.WebView && binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    /**
     * 切换渲染模式
     */
    private fun switchToMode(mode: RenderMode) {
        currentMode = mode
        
        binding.apply {
            webView.visibility = if (mode == RenderMode.WebView) View.VISIBLE else View.GONE
            canvasScrollView.visibility = if (mode == RenderMode.Canvas) View.VISIBLE else View.GONE
            composeView.visibility = if (mode == RenderMode.Compose) View.VISIBLE else View.GONE

            tvRenderMode.setText(mode.titleResId)
            tvRenderInfo.setText(mode.descriptionResId)
        }
    }

    /**
     * 检查是否为深色模式
     */
    private fun isDarkMode(): Boolean {
        return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
                Configuration.UI_MODE_NIGHT_YES
    }

    companion object {
        private const val MATH_FORMULAS_URL = "file:///android_asset/math_formulas.html"
    }
}
