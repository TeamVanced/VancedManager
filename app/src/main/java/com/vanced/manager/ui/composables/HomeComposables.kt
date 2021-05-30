package com.vanced.manager.ui.composables

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LinkCard(
    @DrawableRes icon: Int,
    title: String,
    link: String
) {
    val context = LocalContext.current
    val customTabs = remember { CustomTabsIntent.Builder().build() }
    ManagerCard(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable {
                customTabs.launchUrl(context, Uri.parse(link))
            }
            .size(width = 120.dp, height = 100.dp)
    ) {
        CompositionLocalProvider(LocalContentColor provides managerTextColor()) {
            Column(
                modifier = Modifier.padding(all = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = icon),
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun <T> ScrollableLinkRow(
    items: List<T>,
    content: @Composable (T) -> Unit
) {
    val state = rememberLazyListState()
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = state
    ) {
        itemsIndexed(items) { index, item ->
            content(item)
            if (index < items.size - 1) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}