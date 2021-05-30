package com.vanced.manager.ui.layouts

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanced.manager.ui.composables.HeaderCard
import com.vanced.manager.ui.composables.ManagerCard
import com.vanced.manager.ui.composables.ManagerLazyColumn
import com.vanced.manager.ui.composables.managerTextColor
import com.vanced.manager.ui.theme.ComposeTestTheme
import com.vanced.manager.ui.theme.vancedBlue
import com.vanced.manager.ui.theme.vancedRed

data class Credit(
    val creditName: String,
    val persons: List<String>
)

data class Source(
    val sourceLink: String
)

private val credits = listOf(
    Credit(
        creditName = "Vanced Team",
        persons = listOf(
            "xfileFIN",
            "ZaneZam",
            "Laura Almeida",
            "KevinX8"
        )
    ),
    Credit(
        creditName = "Manager Team",
        persons = listOf(
            "Xinto",
            "Koopah",
            "Logan"
        )
    ),
    Credit(
        creditName = "Other Contributors",
        persons = listOf(
            "bhatVikrant",
            "bawm",
            "AioiLight",
            "HaliksaR"
        )
    )
)

private val sources = listOf(
    Source(
        ""
    ),
    Source(
        ""
    )
)

@ExperimentalStdlibApi
@Composable
@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
fun AboutDarkMode() {
    ComposeTestTheme {
        AboutLayout()
    }
}

@ExperimentalStdlibApi
@Composable
@Preview(
    showSystemUi = true
)
fun AboutLightMode() {
    MaterialTheme {
        AboutLayout()
    }
}

@ExperimentalStdlibApi
@Composable
fun AboutLayout() {
    ManagerLazyColumn {
        item {
            ManagerCard {
                Column(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    vancedBlue,
                                    vancedRed
                                ),
                            )
                        )
                ) {
                    Spacer(modifier = Modifier.size(width = 0.dp, height = 8.dp))
                    Text(
                        text = "Vanced Manager",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 30.sp,
                        color = Color.White
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Re")
                            withStyle(style = SpanStyle(Color(0xFFBBB529))) {
                                append("@Compose")
                            }
                            append("d")
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.size(width = 0.dp, height = 8.dp))
                }
            }
        }

        items(credits) { credit ->
            Spacer(modifier = Modifier.size(width = 0.dp, height = 12.dp))
            CreditsCard(creditName = credit.creditName, persons = credit.persons)
        }

        item {
            Spacer(modifier = Modifier.size(width = 0.dp, height = 12.dp))
            HeaderCard(
                headerName = "Sources"
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    sources.forEach { _ ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.Circle, contentDescription = null, modifier = Modifier.size(36.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

@Composable
@ExperimentalStdlibApi
fun CreditsCard(
    creditName: String,
    persons: List<String>
) {
    HeaderCard(
        headerName = creditName
    ) {
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium,
            LocalContentColor provides managerTextColor()
        ) {
            Text(
                text = persons.joinToString("\n"),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                fontSize = 13.sp
            )
        }
        Spacer(modifier = Modifier.size(width = 0.dp, height = 2.dp))
    }
}