package com.example.mathwebview.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mathwebview.R
import com.example.mathwebview.data.model.FormulaItem
import com.example.mathwebview.util.LaTeXRenderer

/**
 * 数学公式 Canvas 视图
 *
 * 使用 AndroidMath 库将 LaTeX 公式渲染为 Bitmap，然后通过 Canvas 绘制。
 * 支持自定义样式、渐变背景和卡片式布局。
 *
 * ## 特性
 * - 纯原生渲染，无需 WebView
 * - 预渲染公式到 Bitmap，提高绘制效率
 * - 自动计算内容高度
 * - 支持深色模式
 *
 * ## 使用示例
 * ```xml
 * <com.example.mathwebview.ui.view.MathCanvasView
 *     android:id="@+id/mathCanvasView"
 *     android:layout_width="match_parent"
 *     android:layout_height="wrap_content" />
 * ```
 */
class MathCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // region 颜色资源
    private val colorPrimary = ContextCompat.getColor(context, R.color.primary)
    private val colorTextPrimary = ContextCompat.getColor(context, R.color.text_primary)
    private val colorTextHint = ContextCompat.getColor(context, R.color.text_hint)
    private val colorFormulaCard = ContextCompat.getColor(context, R.color.formula_card_background)
    private val colorSurface = ContextCompat.getColor(context, R.color.surface)
    private val colorGradientStart = ContextCompat.getColor(context, R.color.gradient_start)
    private val colorGradientEnd = ContextCompat.getColor(context, R.color.gradient_end)
    private val colorLatexText = ContextCompat.getColor(context, R.color.latex_text)
    // endregion

    // region 绘制工具
    private val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorPrimary
        textSize = 56f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val subtitlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorPrimary
        textSize = 44f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorTextPrimary
        textSize = 36f
    }

    private val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorFormulaCard
        style = Paint.Style.FILL
    }

    private val accentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorPrimary
    }

    private val bgPaint = Paint()

    private val surfacePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorSurface
        style = Paint.Style.FILL
    }

    private val footerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorTextHint
        textSize = 36f
        textAlign = Paint.Align.CENTER
    }

    private val linePaint = Paint().apply {
        color = colorPrimary
        strokeWidth = 6f
    }
    // endregion

    // region 状态
    private var isInitialized = false
    private val formulaBitmaps = mutableMapOf<String, Bitmap?>()
    private var calculatedHeight = DEFAULT_HEIGHT
    private val formulas: List<FormulaItem>
    // endregion

    init {
        formulas = buildFormulaList()
        initLatexMath()
    }

    /**
     * 构建公式列表，包括章节标题和公式项
     */
    private fun buildFormulaList(): List<FormulaItem> = listOf(
        // 代数
        FormulaItem(context.getString(R.string.section_algebra), null),
        FormulaItem(context.getString(R.string.formula_quadratic), "x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}"),
        FormulaItem(context.getString(R.string.formula_square), "(a + b)^2 = a^2 + 2ab + b^2"),
        // 微积分
        FormulaItem(context.getString(R.string.section_calculus), null),
        FormulaItem(context.getString(R.string.formula_derivative), "f'(x) = \\lim_{h \\to 0} \\frac{f(x+h) - f(x)}{h}"),
        FormulaItem(context.getString(R.string.formula_integral), "\\int_a^b f(x) \\, dx = F(b) - F(a)"),
        FormulaItem(context.getString(R.string.formula_taylor), "e^x = \\sum_{n=0}^{\\infty} \\frac{x^n}{n!}"),
        // 线性代数
        FormulaItem(context.getString(R.string.section_linear_algebra), null),
        FormulaItem(context.getString(R.string.formula_matrix), "\\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix} \\begin{pmatrix} e \\\\ f \\end{pmatrix} = \\begin{pmatrix} ae+bf \\\\ ce+df \\end{pmatrix}"),
        FormulaItem(context.getString(R.string.formula_determinant), "\\det \\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix} = ad - bc"),
        // 统计学
        FormulaItem(context.getString(R.string.section_statistics), null),
        FormulaItem(context.getString(R.string.formula_normal_dist), "f(x) = \\frac{1}{\\sigma\\sqrt{2\\pi}} e^{-\\frac{(x-\\mu)^2}{2\\sigma^2}}"),
        FormulaItem(context.getString(R.string.formula_bayes), "P(A|B) = \\frac{P(B|A) \\cdot P(A)}{P(B)}"),
        // 著名公式
        FormulaItem(context.getString(R.string.section_famous), null),
        FormulaItem(context.getString(R.string.formula_euler), "e^{i\\pi} + 1 = 0"),
        FormulaItem(context.getString(R.string.formula_pythagorean), "a^2 + b^2 = c^2"),
        FormulaItem(context.getString(R.string.formula_mass_energy), "E = mc^2")
    )

    /**
     * 初始化 LaTeX 渲染引擎并预渲染公式
     */
    private fun initLatexMath() {
        try {
            isInitialized = LaTeXRenderer.init(context)
            if (isInitialized) {
                preRenderFormulas()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            isInitialized = false
        }
    }

    /**
     * 预渲染所有公式为 Bitmap
     */
    private fun preRenderFormulas() {
        if (!isInitialized) return

        formulas
            .filter { it.latex != null && !formulaBitmaps.containsKey(it.latex) }
            .forEach { item ->
                formulaBitmaps[item.latex!!] = LaTeXRenderer.renderToBitmap(
                    latex = item.latex,
                    textSize = FORMULA_TEXT_SIZE,
                    textColor = colorLatexText,
                    maxWidth = FORMULA_MAX_WIDTH
                )
            }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else -> widthSize
        }

        calculateContentHeight()

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> calculatedHeight.coerceAtMost(heightSize)
            else -> calculatedHeight
        }

        setMeasuredDimension(width, height)
    }

    /**
     * 计算内容总高度
     */
    private fun calculateContentHeight() {
        var height = HEADER_HEIGHT

        for (item in formulas) {
            height += if (item.isSection) {
                SECTION_HEIGHT
            } else {
                val bitmap = formulaBitmaps[item.latex]
                val cardHeight = if (bitmap != null) {
                    (bitmap.height + CARD_PADDING).coerceAtLeast(MIN_CARD_HEIGHT)
                } else {
                    MIN_CARD_HEIGHT
                }
                cardHeight + CARD_MARGIN
            }
        }

        height += FOOTER_HEIGHT
        calculatedHeight = height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawBackground(canvas)
        drawContainer(canvas)

        var y = INITIAL_Y
        y = drawTitle(canvas, context.getString(R.string.title_android_math), y)
        y += TITLE_MARGIN

        for (item in formulas) {
            y = if (item.isSection) {
                drawSection(canvas, item.title, PADDING, y)
            } else {
                drawFormulaCard(canvas, item.title, item.latex!!, PADDING, y)
            }
        }

        y += FOOTER_TOP_MARGIN
        canvas.drawText(context.getString(R.string.footer_android_math), width / 2f, y, footerPaint)
    }

    private fun drawBackground(canvas: Canvas) {
        bgPaint.shader = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            intArrayOf(colorGradientStart, colorGradientEnd),
            null,
            Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
    }

    private fun drawContainer(canvas: Canvas) {
        val containerRect = RectF(
            CONTAINER_MARGIN, CONTAINER_MARGIN,
            width - CONTAINER_MARGIN, height - CONTAINER_MARGIN
        )
        canvas.drawRoundRect(containerRect, CORNER_RADIUS, CORNER_RADIUS, surfacePaint)
    }

    private fun drawTitle(canvas: Canvas, text: String, y: Float): Float {
        val paint = Paint(titlePaint).apply { textAlign = Paint.Align.CENTER }
        canvas.drawText(text, width / 2f, y, paint)

        val lineY = y + TITLE_LINE_OFFSET
        canvas.drawLine(
            width / 2f - TITLE_LINE_WIDTH / 2,
            lineY,
            width / 2f + TITLE_LINE_WIDTH / 2,
            lineY,
            linePaint
        )

        return y + TITLE_HEIGHT
    }

    private fun drawSection(canvas: Canvas, text: String, x: Float, y: Float): Float {
        canvas.drawText(text, x, y, subtitlePaint)
        return y + SECTION_HEIGHT
    }

    private fun drawFormulaCard(
        canvas: Canvas,
        title: String,
        latex: String,
        x: Float,
        y: Float
    ): Float {
        val bitmap = formulaBitmaps[latex]
        val cardHeight = if (bitmap != null) {
            (bitmap.height + CARD_PADDING).coerceAtLeast(MIN_CARD_HEIGHT).toFloat()
        } else {
            MIN_CARD_HEIGHT.toFloat()
        }

        val cardWidth = width - CARD_HORIZONTAL_MARGIN
        val cardRect = RectF(x, y, x + cardWidth, y + cardHeight)

        canvas.drawRoundRect(cardRect, CARD_CORNER_RADIUS, CARD_CORNER_RADIUS, cardPaint)
        canvas.drawRoundRect(
            RectF(x, y, x + ACCENT_WIDTH, y + cardHeight),
            ACCENT_CORNER_RADIUS, ACCENT_CORNER_RADIUS,
            accentPaint
        )

        canvas.drawText(title, x + CARD_CONTENT_PADDING, y + TITLE_Y_OFFSET, labelPaint)

        if (bitmap != null) {
            val bitmapX = x + (cardWidth - bitmap.width) / 2
            val bitmapY = y + BITMAP_Y_OFFSET
            canvas.drawBitmap(bitmap, bitmapX, bitmapY, null)
        } else if (!isInitialized) {
            val errorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = colorTextHint
                textSize = ERROR_TEXT_SIZE
            }
            canvas.drawText(
                context.getString(R.string.error_latex_init),
                x + CARD_CONTENT_PADDING,
                y + ERROR_Y_OFFSET,
                errorPaint
            )
        }

        return y + cardHeight + CARD_MARGIN
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        formulaBitmaps.values.forEach { it?.recycle() }
        formulaBitmaps.clear()
    }

    companion object {
        private const val DEFAULT_HEIGHT = 1800
        private const val FORMULA_TEXT_SIZE = 24f
        private const val FORMULA_MAX_WIDTH = 800

        private const val HEADER_HEIGHT = 160
        private const val SECTION_HEIGHT = 50
        private const val MIN_CARD_HEIGHT = 140
        private const val CARD_PADDING = 80
        private const val CARD_MARGIN = 24
        private const val FOOTER_HEIGHT = 80

        private const val INITIAL_Y = 80f
        private const val PADDING = 40f
        private const val TITLE_MARGIN = 20f
        private const val TITLE_HEIGHT = 60f
        private const val TITLE_LINE_OFFSET = 20f
        private const val TITLE_LINE_WIDTH = 300f
        private const val FOOTER_TOP_MARGIN = 40f

        private const val CONTAINER_MARGIN = 20f
        private const val CORNER_RADIUS = 32f
        private const val CARD_CORNER_RADIUS = 24f
        private const val CARD_HORIZONTAL_MARGIN = 80f
        private const val CARD_CONTENT_PADDING = 24f
        private const val ACCENT_WIDTH = 8f
        private const val ACCENT_CORNER_RADIUS = 4f
        private const val TITLE_Y_OFFSET = 40f
        private const val BITMAP_Y_OFFSET = 55f
        private const val ERROR_TEXT_SIZE = 32f
        private const val ERROR_Y_OFFSET = 90f
    }
}
