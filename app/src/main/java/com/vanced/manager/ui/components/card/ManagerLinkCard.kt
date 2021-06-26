package com.vanced.manager.ui.components.card

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.vanced.manager.ui.preferences.holder.useCustomTabsPref

@Composable
fun ManagerLinkCard(
    title: String,
    @DrawableRes icon: Int,
    link: String
) {
    val context = LocalContext.current
    val customTabs = remember { CustomTabsIntent.Builder().build() }
    val uri = remember { Uri.parse(link) }
    val intent = remember { Intent(Intent.ACTION_VIEW, uri) }
    val useCustomTabs by useCustomTabsPref
    ManagerClickableItemCard(
        title = title,
        icon = icon
    ) {
        if (useCustomTabs) {
            customTabs.launchUrl(context, uri)
        } else {
            context.startActivity(intent)
        }
    }
}