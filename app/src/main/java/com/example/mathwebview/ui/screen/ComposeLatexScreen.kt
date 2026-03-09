package com.example.mathwebview.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathwebview.R
import com.example.mathwebview.data.model.MathFormulas
import com.example.mathwebview.ui.components.FormulaCard
import com.example.mathwebview.ui.components.SectionTitle

/**
 * Compose LaTeX 渲染屏幕
 *
 * 使用 Jetpack Compose 和 latex-renderer 库展示数学公式。
 * 支持深色模式和自动主题适配。
 */
@Composable
fun ComposeLatexScreen() {
    val scrollState = rememberScrollState()

    val primary = colorResource(R.color.primary)
    val gradientStart = colorResource(R.color.gradient_start)
    val gradientEnd = colorResource(R.color.gradient_end)
    val cardBackground = colorResource(R.color.card_background)
    val textSecondary = colorResource(R.color.text_secondary)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(colors = listOf(gradientStart, gradientEnd)))
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScreenHeader(
                    title = stringResource(R.string.title_compose_latex),
                    color = primary
                )

                FormulaContent()

                ScreenFooter(
                    text = stringResource(R.string.footer_compose_latex),
                    color = textSecondary
                )
            }
        }
    }
}

/**
 * 屏幕头部组件
 */
@Composable
private fun ScreenHeader(
    title: String,
    color: androidx.compose.ui.graphics.Color
) {
    Text(
        text = title,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = color
    )

    HorizontalDivider(
        modifier = Modifier
            .width(200.dp)
            .padding(vertical = 12.dp),
        thickness = 3.dp,
        color = color
    )

    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * 公式内容区域
 * 使用预定义的公式数据模型渲染所有章节
 */
@Composable
private fun FormulaContent() {
    MathFormulas.allSections.forEach { section ->
        SectionTitle(stringResource(section.titleResId))
        
        section.formulas.forEach { formula ->
            FormulaCard(
                title = stringResource(formula.titleResId),
                latex = formula.latex
            )
        }
    }
}

/**
 * 屏幕底部组件
 */
@Composable
private fun ScreenFooter(
    text: String,
    color: androidx.compose.ui.graphics.Color
) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = text,
        fontSize = 14.sp,
        color = color,
        textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(16.dp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ComposeLatexScreenPreview() {
    ComposeLatexScreen()
}
