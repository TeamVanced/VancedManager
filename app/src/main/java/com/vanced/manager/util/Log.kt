package com.vanced.manager.util

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

data class LogContent(
    val body: AnnotatedString,
)

val logs = mutableListOf<LogContent>()

fun log(tag: String, message: String) {
    Log.i(tag, message)
    logs.add(
        LogContent(
            body = buildAnnotatedString {
                withStyle(SpanStyle(
                    color = Color(0xFF2E73FF),
                    fontWeight = FontWeight.Bold
                )) {
                    append("$tag:")
                }
                append("")
                withStyle(SpanStyle(color = Color.Magenta)) {
                    append(message)
                }
            }
        )
    )
}