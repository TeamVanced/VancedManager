package com.vanced.manager.ui.component.card

import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.vanced.manager.core.preferences.holder.useCustomTabsPref

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
    ManagerItemCard(
        title = title,
        icon = icon
    ) {
        if (useCustomTabsPref) {
            customTabs.launchUrl(context, uri)
        } else {
            context.startActivity(intent)
        }
    }
}