package com.vanced.manager.ui.layouts

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastForEach
import com.vanced.manager.R
import com.vanced.manager.ui.components.card.ManagerLinkCard
import com.vanced.manager.ui.components.card.ManagerThemedCard
import com.vanced.manager.ui.components.layout.ManagerScrollableColumn
import com.vanced.manager.ui.components.layout.ScrollableItemRow
import com.vanced.manager.ui.components.list.ManagerListItem
import com.vanced.manager.ui.components.text.ManagerText
import com.vanced.manager.ui.resources.managerString
import com.vanced.manager.ui.widgets.layout.CategoryLayout

data class Person(
    val name: String,
    val contribution: String
)

data class Credit(
    @StringRes val nameId: Int,
    val persons: List<Person>
)

data class Source(
    @StringRes val nameId: Int,
    @DrawableRes val iconId: Int,
    val link: String
)

private val credits = listOf(
    Credit(
        nameId = R.string.about_category_credits_vanced_team,
        persons = listOf(
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
            )
        )
    ),
    Credit(
        nameId = R.string.about_category_credits_manager_devs,
        persons = listOf(
            Person(
                name = "Xinto",
                contribution = "Manager Core"
            ),
            Person(
                name = "Koopah",
                contribution = "Root installer"
            ),
            Person(
                name = "Logan",
                contribution = "UI"
            ),
            Person(
                name = "HaliksaR",
                contribution = "Refactoring, UI"
            ),
        )
    ),
    Credit(
        nameId = R.string.about_category_credits_other,
        persons = listOf(
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
        )
    )
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

@Composable
fun AboutLayout() {
    ManagerScrollableColumn(
        itemSpacing = 12.dp
    ) {
        AboutManagerCard()
        credits.fastForEach { credit ->
            CategoryLayout(
                categoryName = managerString(stringId = credit.nameId),
                categoryNameSpacing = 4.dp
            ) {
                Column {
                    credit.persons.fastForEach { person ->
                        ManagerListItem(
                            title = {
                                ManagerText(
                                    text = person.name,
                                    textStyle = MaterialTheme.typography.h6
                                )
                            },
                            description = {
                                ManagerText(
                                    text = person.contribution,
                                    textStyle = MaterialTheme.typography.subtitle1
                                )
                            }
                        )
                    }
                }
            }
        }
        CategoryLayout(
            categoryName = managerString(
                stringId = R.string.about_category_sources
            ),
        ) {
            ScrollableItemRow(items = sources) { source ->
                ManagerLinkCard(
                    title = managerString(source.nameId),
                    icon = source.iconId,
                    link = source.link
                )
            }
        }
    }
}

@Composable
fun AboutManagerCard() {
    ManagerThemedCard {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = managerString(stringId = R.string.app_name),
                modifier = Modifier
                    .padding(top = 8.dp),
                fontSize = 30.sp,
            )
            Text(
                text = buildAnnotatedString {
                    append("Re")
                    withStyle(style = SpanStyle(Color(0xFFBBB529))) {
                        append("@Compose")
                    }
                    append("d")
                },
                modifier = Modifier
                    .padding(bottom = 8.dp),
                fontSize = 16.sp,
            )
        }
    }
}