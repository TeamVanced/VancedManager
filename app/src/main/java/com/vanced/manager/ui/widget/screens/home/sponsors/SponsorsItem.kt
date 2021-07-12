package com.vanced.manager.ui.widget.screens.home.sponsors

import androidx.compose.runtime.Composable
import com.vanced.manager.ui.component.card.ManagerLinkCard
import com.vanced.manager.ui.component.layout.ScrollableItemRow
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