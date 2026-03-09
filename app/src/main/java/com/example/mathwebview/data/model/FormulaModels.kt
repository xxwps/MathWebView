package com.example.mathwebview.data.model

import androidx.annotation.StringRes
import com.example.mathwebview.R

/**
 * 公式项数据模型
 *
 * @property title 公式标题
 * @property latex LaTeX 公式字符串，为 null 表示这是一个章节标题
 */
data class FormulaItem(
    val title: String,
    val latex: String?
) {
    /** 判断是否为章节标题 */
    val isSection: Boolean get() = latex == null
}

/**
 * 公式章节数据模型
 *
 * @property titleResId 章节标题资源 ID
 * @property formulas 该章节下的公式列表
 */
data class FormulaSection(
    @StringRes val titleResId: Int,
    val formulas: List<Formula>
)

/**
 * 单个公式数据模型
 *
 * @property titleResId 公式标题资源 ID
 * @property latex LaTeX 公式字符串
 */
data class Formula(
    @StringRes val titleResId: Int,
    val latex: String
)

/**
 * 预定义的数学公式集合
 * 包含代数、微积分、线性代数、统计学和著名公式
 */
object MathFormulas {
    
    /** 代数公式 */
    val algebraFormulas = listOf(
        Formula(R.string.formula_quadratic, "x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}"),
        Formula(R.string.formula_square, "(a + b)^2 = a^2 + 2ab + b^2")
    )
    
    /** 微积分公式 */
    val calculusFormulas = listOf(
        Formula(R.string.formula_derivative, "f'(x) = \\lim_{h \\to 0} \\frac{f(x+h) - f(x)}{h}"),
        Formula(R.string.formula_integral, "\\int_a^b f(x) \\, dx = F(b) - F(a)"),
        Formula(R.string.formula_taylor, "e^x = \\sum_{n=0}^{\\infty} \\frac{x^n}{n!}")
    )
    
    /** 线性代数公式 */
    val linearAlgebraFormulas = listOf(
        Formula(R.string.formula_matrix, "\\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix} \\begin{pmatrix} e \\\\ f \\end{pmatrix} = \\begin{pmatrix} ae+bf \\\\ ce+df \\end{pmatrix}"),
        Formula(R.string.formula_determinant, "\\det \\begin{pmatrix} a & b \\\\ c & d \\end{pmatrix} = ad - bc")
    )
    
    /** 统计学公式 */
    val statisticsFormulas = listOf(
        Formula(R.string.formula_normal_dist, "f(x) = \\frac{1}{\\sigma\\sqrt{2\\pi}} e^{-\\frac{(x-\\mu)^2}{2\\sigma^2}}"),
        Formula(R.string.formula_bayes, "P(A|B) = \\frac{P(B|A) \\cdot P(A)}{P(B)}")
    )
    
    /** 著名公式 */
    val famousFormulas = listOf(
        Formula(R.string.formula_euler, "e^{i\\pi} + 1 = 0"),
        Formula(R.string.formula_pythagorean, "a^2 + b^2 = c^2"),
        Formula(R.string.formula_mass_energy, "E = mc^2")
    )
    
    /** 所有章节 */
    val allSections = listOf(
        FormulaSection(R.string.section_algebra, algebraFormulas),
        FormulaSection(R.string.section_calculus, calculusFormulas),
        FormulaSection(R.string.section_linear_algebra, linearAlgebraFormulas),
        FormulaSection(R.string.section_statistics, statisticsFormulas),
        FormulaSection(R.string.section_famous, famousFormulas)
    )
}
