package com.vanced.manager.ui.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import com.vanced.manager.ui.theme.SmallShape

private const val TransitionDuration = 200

@ExperimentalAnimationApi
@Composable
fun ManagerDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val expandedStates = remember { MutableTransitionState(false) }
    expandedStates.targetState = expanded

    val transition = updateTransition(expandedStates, "ManagerDropDownMenu")

    val alphaAndScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = TransitionDuration) },
        label = "AlphaAndScale"
    ) {
        if (it) 1f else 0f
    }

    if (expandedStates.currentState || expandedStates.targetState) {
        val density = LocalDensity.current
        val popupPositionProvider = ManagerDropdownMenuPopupPositionProvider(density)

        Popup(
            popupPositionProvider = popupPositionProvider,
            onDismissRequest = onDismissRequest,
            properties = PopupProperties(focusable = true)
        ) {
            OutlinedCard(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .alpha(alphaAndScale)
                    .scale(alphaAndScale),
                elevation = CardDefaults.elevatedCardElevation()
            ) {
                Column(content = content)
            }
        }
    }
}

@Composable
fun ManagerDropdownMenuItem(
    title: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        onClick = onClick,
        modifier = Modifier.clip(SmallShape),
        text = {
            ManagerText(text = title)
        }
    )
}

//Kanged from Menu.kt
private data class ManagerDropdownMenuPopupPositionProvider(
    val density: Density
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val verticalMargin = with(density) { 48.dp.roundToPx() }

        //Compute horizontal position.
        val toRight = anchorBounds.left
        val toLeft = anchorBounds.right - popupContentSize.width
        val toDisplayRight = windowSize.width - popupContentSize.width
        val toDisplayLeft = 0
        val x = if (layoutDirection == LayoutDirection.Ltr) {
            sequenceOf(toRight, toLeft, toDisplayRight)
        } else {
            sequenceOf(toLeft, toRight, toDisplayLeft)
        }.firstOrNull {
            it >= 0 && it + popupContentSize.width <= windowSize.width
        } ?: toLeft

        // Compute vertical position.
        val toBottom = maxOf(anchorBounds.bottom, verticalMargin)
        val toTop = anchorBounds.top - popupContentSize.height
        val toCenter = anchorBounds.top - popupContentSize.height / 2
        val toDisplayBottom = windowSize.height - popupContentSize.height - verticalMargin
        val y = sequenceOf(toBottom, toTop, toCenter, toDisplayBottom).firstOrNull {
            it >= verticalMargin && it + popupContentSize.height <= windowSize.height - verticalMargin
        } ?: toTop

        return IntOffset(x, y)
    }

}