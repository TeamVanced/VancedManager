package com.vanced.manager.ui.layouts

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.domain.model.Link
import com.vanced.manager.ui.composables.*
import com.vanced.manager.ui.viewmodel.HomeViewModel
import org.koin.androidx.compose.getViewModel

private val sponsors = listOf(
    Link(
        title = "Brave"
    ),
    Link(
        title = "Adguard"
    )
)

//Lol
private val socialMedia = listOf(
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
    Link(
        title = "Instagram"
    ),
)

@ExperimentalAnimationApi
@Composable
@Preview
fun HomeLayout() {
    val viewModel: HomeViewModel = getViewModel()
    val refreshState = rememberSwipeRefreshState(isRefreshing = viewModel.isFetching)
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
        ManagerScrollableColumn {
            HomeHeaderView(headerName = "Apps") {
                viewModel.apps.fastForEachIndexed { index,  app ->
                    val rememberedApp by remember { app }
                    AppCard(rememberedApp)
                    if (index != viewModel.apps.size - 1) {
                        Spacer(modifier = Modifier.size(height = 8.dp, width = 0.dp))
                    }
                }
            }
            ManagerCardSeparator()
            HomeHeaderView(
                modifier = Modifier.fillMaxWidth(),
                headerName = "Support Us"
            ) {
               ScrollableLinkRow(items = sponsors) { sponsor ->
                    LinkCard(
                        icon = R.drawable.ic_android_black_24dp,
                        title = sponsor.title,
                        link = "https://m.youtube.com"
                    )
                }
            }
            ManagerCardSeparator()
            HomeHeaderView(
                modifier = Modifier.fillMaxWidth(),
                headerName = "Social media"
            ) {
                ScrollableLinkRow(items = socialMedia) { socialMedia ->
                    LinkCard(
                        icon = R.drawable.ic_android_black_24dp,
                        title = socialMedia.title,
                        link = "https://m.youtube.com"
                    )
                }
            }
        }
    }
}