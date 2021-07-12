package com.vanced.manager.ui.widget.screens.home.socialmedia

import androidx.compose.runtime.Composable
import com.vanced.manager.ui.component.card.ManagerLinkCard
import com.vanced.manager.ui.component.layout.ScrollableItemRow
import com.vanced.manager.util.socialMedia

@Composable
fun HomeSocialMediaItem() {
    ScrollableItemRow(items = socialMedia) { socialMedia ->
        ManagerLinkCard(
            icon = socialMedia.icon,
            title = socialMedia.title,
            link = socialMedia.link
        )
    }
}