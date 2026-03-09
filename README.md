# MathWebView

<p align="center">
  <img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" width="100" alt="App Icon"/>
</p>

<p align="center">
  <strong>Android 数学公式渲染演示应用</strong>
</p>

<p align="center">
  展示三种不同的 LaTeX 渲染方案对比：WebView + MathJax、AndroidMath 原生渲染、Compose LaTeX Renderer
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform"/>
  <img src="https://img.shields.io/badge/API-24%2B-brightgreen.svg" alt="API"/>
  <img src="https://img.shields.io/badge/Kotlin-2.1.0-purple.svg" alt="Kotlin"/>
  <img src="https://img.shields.io/badge/Compose-2024.12.01-blue.svg" alt="Compose"/>
</p>

---

## 功能特性

- **三种渲染模式一键切换** - 通过 Tab 快速对比不同渲染方案的效果
- **Material Design 3** - 现代化 UI 设计，支持深色模式
- **丰富的公式展示** - 涵盖代数、微积分、线性代数、统计学等领域
- **离线支持** - AndroidMath 和 Compose 方案完全离线可用

## 渲染方案对比

| 特性 | WebView + MathJax | AndroidMath | Compose LaTeX |
|------|:-----------------:|:-----------:|:-------------:|
| **LaTeX 支持度** | ⭐⭐⭐⭐⭐ 完整 | ⭐⭐⭐ 基础数学 | ⭐⭐⭐⭐ 372+ 特性 |
| **网络依赖** | ❌ 需要（首次） | ✅ 不需要 | ✅ 不需要 |
| **渲染速度** | 🐢 较慢 | 🚀 快 | 🚀 快 |
| **内存占用** | 📈 较高 | 📉 低 | 📊 中等 |
| **实现语言** | JavaScript | 纯 Kotlin | Kotlin/Compose |
| **跨平台** | Web 兼容 | 仅 Android | 全平台 |
| **深色模式** | ✅ | ✅ | ✅ |

### 选择建议

- **WebView + MathJax** - 需要完整 LaTeX 支持，对性能要求不高
- **AndroidMath** - 追求最佳性能，公式相对简单
- **Compose LaTeX** - 使用 Jetpack Compose，需要跨平台

## 技术栈

- **语言**: Kotlin 2.1.0
- **UI 框架**: Jetpack Compose + View System
- **最低 SDK**: API 24 (Android 7.0)
- **目标 SDK**: API 36
- **构建工具**: Gradle 8.9 + AGP 8.7.0

### 依赖库

```kotlin
// AndroidMath - 纯 Kotlin LaTeX 渲染
implementation("com.github.gregcockroft:AndroidMath:1.1.0")

// latex-renderer - Compose 跨平台 LaTeX 渲染
implementation("io.github.huarangmeng:latex-renderer:1.3.0-kt2.1.0")
```

## 项目结构

```
MathWebView/
├── app/
│   ├── src/main/
│   │   ├── assets/
│   │   │   └── math_formulas.html      # MathJax HTML 页面
│   │   ├── java/com/example/mathwebview/
│   │   │   ├── data/
│   │   │   │   └── model/
│   │   │   │       ├── FormulaModels.kt    # 公式数据模型
│   │   │   │       └── RenderMode.kt       # 渲染模式密封类
│   │   │   ├── ui/
│   │   │   │   ├── components/
│   │   │   │   │   └── FormulaCard.kt      # Compose 公式卡片组件
│   │   │   │   ├── screen/
│   │   │   │   │   └── ComposeLatexScreen.kt   # Compose 界面
│   │   │   │   ├── theme/
│   │   │   │   │   └── Color.kt            # 主题颜色定义
│   │   │   │   ├── view/
│   │   │   │   │   └── MathCanvasView.kt   # Canvas 自定义视图
│   │   │   │   └── MainActivity.kt         # 主界面
│   │   │   └── util/
│   │   │       └── LaTeXRenderer.kt        # LaTeX 渲染工具类
│   │   └── res/
│   │       ├── layout/
│   │       ├── values/
│   │       └── values-night/               # 深色模式资源
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml                  # 版本目录
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 快速开始

### 环境要求

- Android Studio Ladybug (2024.2.1) 或更高版本
- JDK 17
- Android SDK 36

### 构建步骤

1. **克隆项目**
   ```bash
   git clone https://github.com/your-username/MathWebView.git
   cd MathWebView
   ```

2. **用 Android Studio 打开项目**

3. **同步 Gradle**（自动下载依赖）

4. **运行应用**
   - 连接 Android 设备或启动模拟器
   - 点击运行按钮

## 使用指南

### LaTeXRenderer 工具类

封装了 AndroidMath 库，提供简洁易用的 API：

```kotlin
// 1. 初始化（在 Application 或 Activity 中）
LaTeXRenderer.init(context)

// 2. 渲染为 Bitmap
val bitmap: Bitmap? = LaTeXRenderer.renderToBitmap(
    latex = "x = \\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}",
    textSize = 20f,
    textColor = Color.BLACK,
    maxWidth = 720
)

// 3. 渲染为 Drawable（用于 ImageView）
val drawable: Drawable? = LaTeXRenderer.renderToDrawable(
    context = context,
    latex = "E = mc^2",
    textSize = 24f
)
imageView.setImageDrawable(drawable)

// 4. 创建 MTMathView 实例
val mathView = LaTeXRenderer.createMathView(
    context = context,
    latex = "\\int_0^1 f(x) dx",
    textSize = 20f
)
layout.addView(mathView)

// 5. 批量渲染
val bitmaps: Map<String, Bitmap?> = LaTeXRenderer.renderBatch(
    latexList = listOf("a^2 + b^2 = c^2", "e^{i\\pi} + 1 = 0"),
    textSize = 20f,
    textColor = Color.BLACK
)
```

### 直接使用 MTMathView

**XML 布局：**
```xml
<com.agog.mathdisplay.MTMathView
    android:id="@+id/mathView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

**Kotlin 代码：**
```kotlin
binding.mathView.apply {
    latex = "\\frac{-b \\pm \\sqrt{b^2-4ac}}{2a}"
    fontSize = 20f
    textColor = Color.BLACK
}
```

### Compose LaTeX Renderer

```kotlin
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig

@Composable
fun FormulaDisplay() {
    Latex(
        latex = "\\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}",
        config = LatexConfig(
            fontSize = 20.sp,
            color = Color.Black,
            darkColor = Color.White  // 自动深色模式支持
        )
    )
}
```

## 支持的 LaTeX 特性

### AndroidMath 支持

- 分数：`\frac{a}{b}`
- 根号：`\sqrt{x}`, `\sqrt[n]{x}`
- 上下标：`x^2`, `x_i`
- 希腊字母：`\alpha`, `\beta`, `\gamma` 等
- 运算符：`\sum`, `\int`, `\prod` 等
- 矩阵：`\begin{pmatrix}...\end{pmatrix}`
- 括号：`\left(`, `\right)`

### latex-renderer 支持 (372+ 特性)

- **数学公式**: 分数、根号、二项式
- **符号 (130+)**: 希腊字母、运算符、箭头、AMS 符号
- **大型运算符 (28)**: 求和、积分、极限
- **矩阵 (8)**: matrix, pmatrix, bmatrix, vmatrix 等
- **环境 (21)**: align, cases, gather 等
- **化学公式**: `\ce{H2O}`, `\ce{H2SO4}`
- **高级功能**: 自动换行、图片导出、无障碍支持

## 代码规范

本项目遵循现代 Android 开发最佳实践：

- ✅ **ViewBinding** - 类型安全的视图绑定
- ✅ **Version Catalog** - 集中管理依赖版本
- ✅ **Sealed Class** - 类型安全的状态管理
- ✅ **KDoc 文档** - 完整的 API 文档注释
- ✅ **分包结构** - 清晰的代码组织 (ui/data/util)
- ✅ **Compose Preview** - 组件预览支持

## GitHub LaTeX 库对比

| 库名 | Stars | 特点 | 依赖方式 |
|------|:-----:|------|:--------:|
| [latex-renderer](https://github.com/huarangmeng/latex) | 59 | Compose 跨平台，372+ 特性 | Maven Central |
| [AndroidMath](https://github.com/gregcockroft/AndroidMath) | 194 | 纯 Kotlin，无需 WebView | JitPack |
| [AndroidLaTeXMath](https://github.com/NanoMichael/AndroidLaTeXMath) | 131 | C++ 实现，高性能 | jcenter |
| [MicroTeX](https://github.com/NanoMichael/MicroTeX) | 588 | 跨平台，C++ 实现 | NDK |
| [kotlitex](https://github.com/karino2/kotlitex) | 13 | KaTeX 的 Kotlin 移植 | JitPack |

## 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add some amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 创建 Pull Request

### 代码风格

- 遵循 [Kotlin 官方编码规范](https://kotlinlang.org/docs/coding-conventions.html)
- 使用 ktlint 进行代码格式化
- 为公共 API 添加 KDoc 文档

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 致谢

- [AndroidMath](https://github.com/gregcockroft/AndroidMath) - 优秀的纯 Kotlin LaTeX 渲染库
- [latex-renderer](https://github.com/huarangmeng/latex) - 功能强大的 Compose LaTeX 渲染库
- [MathJax](https://www.mathjax.org/) - 业界标准的 Web 数学公式渲染引擎

---

<p align="center">
  Made with ❤️ for the Android community
</p>
