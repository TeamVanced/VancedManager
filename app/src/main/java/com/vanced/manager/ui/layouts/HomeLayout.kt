package com.vanced.manager.ui.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.ui.components.layout.ManagerScrollableColumn
import com.vanced.manager.ui.components.layout.ManagerSwipeRefresh
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.utils.defaultContentPaddingVertical
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.vanced.manager.ui.widgets.home.apps.HomeAppsItem
import com.vanced.manager.ui.widgets.home.socialmedia.HomeSocialMediaItem
import com.vanced.manager.ui.widgets.home.sponsors.HomeSponsorsItem
import com.vanced.manager.ui.widgets.layout.CategoryLayout
import org.koin.androidx.compose.getViewModel

@Composable
@Preview
fun HomeLayout() {
    val viewModel: HomeViewModel = getViewModel()
    val isFetching by viewModel.isFetching.observeAsState(false)
    val refreshState = rememberSwipeRefreshState(isRefreshing = isFetching)
    ManagerSwipeRefresh(
        refreshState = refreshState,
        onRefresh = { viewModel.fetch() }
    ) {
        ManagerScrollableColumn(
            contentPaddingVertical = defaultContentPaddingVertical,
            itemSpacing = 18.dp
        ) {
            CategoryLayout(
                categoryName = managerString(
                    stringId = R.string.home_category_apps
                ),
                contentPaddingHorizontal = 0.dp
            ) {
                HomeAppsItem(viewModel, isFetching)
            }
            CategoryLayout(
                categoryName = managerString(
                    stringId = R.string.home_category_support_us
                )
            ) {
                HomeSponsorsItem()
            }
            CategoryLayout(
                categoryName = managerString(
                    stringId = R.string.home_category_social_media
                )
            ) {
                HomeSocialMediaItem()
            }
        }
    }
}