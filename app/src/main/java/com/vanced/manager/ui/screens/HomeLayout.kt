package com.vanced.manager.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vanced.manager.R
import com.vanced.manager.ui.component.layout.ManagerScrollableColumn
import com.vanced.manager.ui.component.layout.ManagerSwipeRefresh
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.util.defaultContentPaddingVertical
import com.vanced.manager.ui.viewmodel.HomeViewModel
import com.vanced.manager.ui.widget.layout.CategoryLayout
import com.vanced.manager.ui.widget.screens.home.apps.HomeAppsItem
import com.vanced.manager.ui.widget.screens.home.socialmedia.HomeSocialMediaItem
import com.vanced.manager.ui.widget.screens.home.sponsors.HomeSponsorsItem
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
                HomeAppsItem(viewModel)
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