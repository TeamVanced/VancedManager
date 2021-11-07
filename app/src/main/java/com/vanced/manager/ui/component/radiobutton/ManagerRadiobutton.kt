package com.vanced.manager.ui.component.radiobutton

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.vanced.manager.ui.component.card.ManagerCard
import com.vanced.manager.ui.component.color.managerAnimatedColor
import com.vanced.manager.ui.theme.MediumShape

@SuppressLint("UnusedTransitionTargetStateParameter")
@Composable
fun ManagerRadiobutton(
    modifier: Modifier,
    isSelected: Boolean,
    shape: Shape = MediumShape,
    onClick: () -> Unit
) {
    val cardColor =
        managerAnimatedColor(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer)
    ManagerCard(
        modifier = modifier,
        onClick = onClick,
        shape = shape,
        backgroundColor = cardColor,
        content = {}
    )
}