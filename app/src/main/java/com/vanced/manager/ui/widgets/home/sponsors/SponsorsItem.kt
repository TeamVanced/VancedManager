package com.vanced.manager.ui.widgets.home.sponsors

import androidx.compose.runtime.Composable
import com.vanced.manager.ui.components.card.ManagerLinkCard
import com.vanced.manager.ui.components.layout.ScrollableItemRow
import com.vanced.manager.util.sponsors

@Composable
fun HomeSponsorsItem() {
    ScrollableItemRow(items = sponsors) { sponsor ->
        ManagerLinkCard(
            icon = sponsor.icon,
            title = sponsor.title,
            link = sponsor.link
        )
    }
}