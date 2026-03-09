package com.example.mathwebview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathwebview.R
import com.hrm.latex.renderer.Latex
import com.hrm.latex.renderer.model.LatexConfig

/**
 * 公式卡片组件
 *
 * 用于展示单个 LaTeX 公式，包含标题和渲染后的公式内容。
 * 左侧带有主题色装饰条。
 *
 * @param title 公式标题
 * @param latex LaTeX 公式字符串
 * @param modifier Modifier 修饰符
 */
@Composable
fun FormulaCard(
    title: String,
    latex: String,
    modifier: Modifier = Modifier
) {
    val primary = colorResource(R.color.primary)
    val cardBackground = colorResource(R.color.formula_card_background)
    val textPrimary = colorResource(R.color.text_primary)
    val latexColor = colorResource(R.color.latex_text)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(IntrinsicSize.Max)
                    .background(
                        primary,
                        RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = textPrimary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Latex(
                        latex = latex,
                        config = LatexConfig(
                            fontSize = 20.sp,
                            color = latexColor,
                            darkColor = latexColor
                        )
                    )
                }
            }
        }
    }
}

/**
 * 章节标题组件
 *
 * @param title 章节标题文本
 * @param modifier Modifier 修饰符
 */
@Composable
fun SectionTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    val primary = colorResource(R.color.primary)
    
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = primary,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun FormulaCardPreview() {
    FormulaCard(
        title = "二次方程求根公式",
        latex = "x = \\frac{-b \\pm \\sqrt{b^2 - 4ac}}{2a}"
    )
}

@Preview(showBackground = true)
@Composable
private fun SectionTitlePreview() {
    SectionTitle(title = "代数")
}
