package com.vanced.manager.ui.layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.domain.model.App
import com.vanced.manager.ui.components.*
import com.vanced.manager.ui.components.card.ManagerLinkCard
import com.vanced.manager.ui.components.color.managerAccentColor
import com.vanced.manager.ui.components.layout.ManagerScrollableColumn
import com.vanced.manager.ui.components.layout.ScrollableItemRow
import com.vanced.manager.ui.utils.defaultContentPaddingVertical
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.vanced.manager.ui.widgets.home.apps.card.AppCard
import com.vanced.manager.ui.widgets.layout.CategoryLayout
import com.vanced.manager.util.socialMedia
import com.vanced.manager.util.sponsors
import org.koin.androidx.compose.getViewModel

@Composable
@Preview
fun HomeLayout() {
    val viewModel: HomeViewModel = getViewModel()
    val isFetching by viewModel.isFetching.observeAsState(false)
    val refreshState = rememberSwipeRefreshState(isRefreshing = isFetching)
    SwipeRefresh(
        state = refreshState,
        onRefresh = { viewModel.fetch() },
        indicator = { state, trigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = trigger,
                scale = true,
                contentColor = managerAccentColor()
            )
        }
    ) {
        ManagerScrollableColumn(
            contentPaddingVertical = defaultContentPaddingVertical,
            itemSpacing = 18.dp
        ) {
            CategoryLayout(
                categoryNameId = R.string.home_category_apps,
                contentPaddingHorizontal = 0.dp
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    viewModel.apps.fastForEach { app ->
                        val observedApp by app.observeAsState(initial = App())
                        AppCard(observedApp, isFetching)
                    }
                }
            }
            CategoryLayout(categoryNameId = R.string.home_category_support_us) {
                ScrollableItemRow(items = sponsors) { sponsor ->
                    ManagerLinkCard(
                        icon = sponsor.icon,
                        title = sponsor.title,
                        link = sponsor.link
                    )
                }
            }
            CategoryLayout(categoryNameId = R.string.home_category_social_media) {
                ScrollableItemRow(items = socialMedia) { socialMedia ->
                    ManagerLinkCard(
                        icon = socialMedia.icon,
                        title = socialMedia.title,
                        link = socialMedia.link
                    )
                }
            }
        }
    }
}