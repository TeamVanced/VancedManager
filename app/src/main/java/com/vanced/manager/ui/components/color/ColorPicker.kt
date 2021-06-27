package com.vanced.manager.ui.components.color

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.orchestra.colorpicker.ColorPicker
import com.vanced.manager.ui.preferences.holder.managerAccentColorPref

@Composable
fun ManagerColorPicker(
    onColorSelected: (color: Long) -> Unit
) {
    val (selectedColor, setColor) = remember { mutableStateOf(ColorEnvelope(0)) }
    ColorPicker(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        onColorListener = { envelope, _ -> setColor(envelope) },
        children = {},
        initialColor = managerAccentColor()
    )
    onColorSelected(selectedColor.color.toLong())
}