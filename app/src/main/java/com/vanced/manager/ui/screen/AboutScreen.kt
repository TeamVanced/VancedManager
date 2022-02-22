package com.vanced.manager.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import com.vanced.manager.BuildConfig
import com.vanced.manager.R
import com.vanced.manager.ui.component.*
import com.vanced.manager.ui.resource.managerString
import com.vanced.manager.ui.theme.LargeShape
import com.vanced.manager.ui.util.DefaultContentPaddingHorizontal
import com.vanced.manager.ui.util.DefaultContentPaddingVertical
import com.vanced.manager.ui.util.Screen
import com.vanced.manager.ui.widget.LinkCard

data class Person(
    val name: String,
    val contribution: String
)

data class Source(
    @StringRes val nameId: Int,
    @DrawableRes val iconId: Int,
    val link: String
)

private val vancedTeam = listOf(
    Person(
        name = "xfileFIN",
        contribution = "Mods, Theming, Support"
    ),
    Person(
        name = "Laura",
        contribution = "Theming, Support"
    ),
    Person(
        name = "ZaneZam",
        contribution = "Publishing, Support"
    ),
    Person(
        name = "KevinX8",
        contribution = "Overlord, Support"
    ),
    Person(
        name = "Xinto",
        contribution = "Vanced Manager"
    )
)

private val otherContributors = listOf(
    Person(
        name = "bhatVikrant",
        contribution = "Website"
    ),
    Person(
        name = "bawm",
        contribution = "Sponsorblock"
    ),
    Person(
        name = "cane",
        contribution = "Sponsorblock"
    ),
    Person(
        name = "Koopah",
        contribution = "Vanced Manager root installer"
    ),
    Person(
        name = "Logan",
        contribution = "Vanced Manager UI"
    ),
    Person(
        name = "HaliksaR",
        contribution = "Vanced Manager Refactoring, UI"
    ),
)

private val sources = listOf(
    Source(
        nameId = R.string.about_sources_source_code,
        iconId = R.drawable.ic_github,
        link = "https://github.com/YTVanced/VancedManager"
    ),
    Source(
        nameId = R.string.about_sources_license,
        iconId = R.drawable.ic_round_assignment_24,
        link = "https://raw.githubusercontent.com/YTVanced/VancedManager/dev/LICENSE"
    )
)

@ExperimentalMaterial3Api
@Composable
fun AboutScreen(
    onToolbarBackButtonClick: () -> Unit
) {
    ManagerScaffold(
        topBar = {
            ManagerSmallTopAppBar(
                title = {
                    ManagerText(managerString(Screen.About.displayName))
                },
                navigationIcon = {
                    IconButton(onClick = onToolbarBackButtonClick) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        ManagerLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            item {
                ManagerElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = LargeShape
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = DefaultContentPaddingVertical,
                                horizontal = DefaultContentPaddingHorizontal
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ManagerText(
                            modifier = Modifier,
                            text = managerString(R.string.app_name),
                            textStyle = MaterialTheme.typography.headlineSmall
                        )
                        ManagerText(
                            modifier = Modifier,
                            text = buildAnnotatedString {
                                append(BuildConfig.VERSION_NAME)
                                val compose = "@Compose"
                                val startIndex = BuildConfig.VERSION_NAME.indexOf(compose)
                                addStyle(
                                    style = SpanStyle(Color(0xFFBBB529)),
                                    start = startIndex,
                                    end = startIndex + compose.length
                                )
                            },
                            textStyle = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }
            managerCategory(categoryName = {
                managerString(R.string.about_category_credits_vanced_team)
            }) {
                items(vancedTeam) { person ->
                    CreditCard(
                        modifier = Modifier.fillMaxWidth(),
                        personName = person.name,
                        personContribution = person.contribution
                    )
                }
            }
            managerCategory(categoryName = {
                managerString(R.string.about_category_credits_other)
            }) {
                items(otherContributors) { person ->
                    CreditCard(
                        modifier = Modifier.fillMaxWidth(),
                        personName = person.name,
                        personContribution = person.contribution
                    )
                }
            }
            managerCategory(categoryName = {
                managerString(R.string.about_category_sources)
            }) {
                item {
                    ManagerLazyRow(modifier = Modifier.fillMaxWidth()) {
                        items(sources) { source ->
                            LinkCard(
                                text = managerString(source.nameId),
                                icon = painterResource(source.iconId),
                                url = source.link
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreditCard(
    personName: String,
    personContribution: String,
    modifier: Modifier = Modifier,
) {
    ManagerElevatedCard(
        modifier = modifier,
        shape = LargeShape
    ) {
        ManagerListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DefaultContentPaddingHorizontal),
            title = {
                ManagerText(
                    text = personName,
                    textStyle = MaterialTheme.typography.titleSmall
                )
            },
            description = {
                ManagerText(
                    text = personContribution,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        )
    }
}