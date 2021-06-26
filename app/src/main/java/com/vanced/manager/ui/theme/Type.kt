package com.vanced.manager.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.vanced.manager.R

private val light = Font(R.font.inter_light, FontWeight.Light)
private val regular = Font(R.font.inter_regular, FontWeight.Normal)
private val medium = Font(R.font.inter_medium, FontWeight.Medium)
private val semibold = Font(R.font.inter_semibold, FontWeight.SemiBold)
private val bold = Font(R.font.inter_bold, FontWeight.Bold)

private val interFontFamily = FontFamily(light, regular, medium, semibold, bold)

// Set of Material typography styles to start with
val typography = Typography(
    defaultFontFamily = interFontFamily,
    h1 = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
    ),
    h2 = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    ),
    h5 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
    ),
    h6 = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    ),
    body1 = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    ),
    body2 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
    ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
    ),
    button = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )
)