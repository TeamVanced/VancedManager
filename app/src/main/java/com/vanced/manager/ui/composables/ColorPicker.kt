package com.vanced.manager.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils

//TODO
@Composable
fun HSLColorPicker(
    modifier: Modifier = Modifier
) {
    val hslColors = floatArrayOf(0f, 1f, 0.5f)
    val hueColors = getColors(hslColors, length = 360, index = 0) { it }
    val saturationColors = getColors(hslColors, length = 11, index = 1) { it / 10 }
    val lightnessColors = getColors(hslColors, length = 11, index = 2) { it / 10 }

    val hueCircle by remember { mutableStateOf(Offset(0f, 0f)) }
    val saturationCircle by remember { mutableStateOf(Offset(0f, 0f)) }
    val lightnessCircle by remember { mutableStateOf(Offset(0f, 0f)) }
    Canvas(modifier = modifier
        .size(250.dp, 250.dp)
        .pointerInput(Unit) {
            detectDragGestures { change: PointerInputChange, dragAmount: Offset ->
                val (changeX, changeY) = change.position
            }
        }
    ) {
        colorArc(
            brush = Brush.sweepGradient(hueColors),
            startAngle = 0f,
            sweepAngle = 360f,
            circleOffset = hueCircle
        )
        inset(
            inset = 60f
        ) {
            colorArc(
                brush = Brush.linearGradient(
                    colors = saturationColors,
                    start = Offset.Infinite,
                    end = Offset.Zero
                ),
                startAngle = 100f,
                sweepAngle = 155f,
                circleOffset = saturationCircle
            )
            colorArc(
                brush = Brush.linearGradient(lightnessColors),
                startAngle = 280f,
                sweepAngle = 155f,
                circleOffset = lightnessCircle
            )
        }
    }
}

fun DrawScope.colorArc(
    brush: Brush,
    startAngle: Float,
    sweepAngle: Float,
    circleOffset: Offset
) {
    drawArc(
        brush = brush,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = false,
        style = Stroke(width = 15f)
    )

    inset(circleOffset.x, circleOffset.y) {
        drawCircle(
            brush = brush,
            radius = 50f
        )
    }
}

fun getColors(
    hslColors: FloatArray,
    length: Int,
    index: Int,
    calculate: (Float) -> Float
): List<Color> {
    val colorsCopy = hslColors.copyOf()
    return List(length) {
        colorsCopy[index] = calculate(it.toFloat())
        Color(ColorUtils.HSLToColor(colorsCopy))
    }
}