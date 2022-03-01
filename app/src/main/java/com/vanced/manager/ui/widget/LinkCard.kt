package com.vanced.manager.ui.widget

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.vanced.manager.ui.component.ManagerElevatedCard
import com.vanced.manager.ui.component.ManagerText
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import org.koin.androidx.compose.inject

//TODO this composable should not handle opening links
@Composable
fun LinkCard(
    text: String,
    icon: Painter,
    url: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val customTabs: CustomTabsIntent by inject()
    val uri = remember { Uri.parse(url) }
    ManagerElevatedCard(
        modifier = modifier
            .height(100.dp)
            .widthIn(min = 100.dp),
        onClick = {
            customTabs.launchUrl(context, uri)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = DefaultContentPaddingHorizontal,
                    vertical = DefaultContentPaddingVertical
                ),
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .align(Alignment.TopStart),
                painter = icon,
                contentDescription = null,
            )
            ManagerText(
                modifier = Modifier.align(Alignment.BottomStart),
                text = text,
                textStyle = MaterialTheme.typography.labelLarge
            )
        }
    }
}