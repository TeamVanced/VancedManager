package com.vanced.manager.ui.compose

//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.Switch
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.em
//import androidx.compose.ui.unit.sp
//import androidx.constraintlayout.compose.ConstraintLayout
//import androidx.core.content.edit
//import androidx.preference.PreferenceManager
//
//@Composable
//@Preview
//inline fun PreferenceCategory(
//    categoryTitle: String,
//    content: @Composable ColumnScope.() -> Unit
//) {
//    Column {
//        Text(
//            categoryTitle,
//            letterSpacing = 0.15.em,
//            color = Color(LocalContext.current.accentColor)
//        )
//        content()
//    }
//}
//
//
//@Composable
//@Preview
//inline fun SwitchPreference(
//    preferenceTitle: String,
//    preferenceDescription: String? = null,
//    preferenceKey: String,
//    defValue: Boolean = true,
//    crossinline onCheckedChange: (Boolean) -> Unit = {}
//) {
//    val prefs = PreferenceManager.getDefaultSharedPreferences(LocalContext.current)
//    val isChecked = remember { mutableStateOf(prefs.getBoolean(preferenceKey, defValue)) }
//    ConstraintLayout(modifier = Modifier.padding(12.dp, 4.dp).clickable {
//        isChecked.value = !isChecked.value
//    }) {
//        val (title, description, switch) = createRefs()
//        Text(preferenceTitle, fontSize = 16.sp, modifier = Modifier.constrainAs(title) {
//            top.linkTo(parent.top)
//            start.linkTo(parent.start)
//            end.linkTo(switch.start, 4.dp)
//            if (preferenceDescription != null) {
//                bottom.linkTo(description.top)
//            } else {
//                bottom.linkTo(parent.bottom)
//            }
//        })
//        if (preferenceDescription != null) {
//            Text(preferenceDescription, fontSize = 13.sp, modifier = Modifier.constrainAs(description) {
//                top.linkTo(title.bottom)
//                start.linkTo(parent.start)
//                end.linkTo(switch.start, 8.dp)
//            })
//        }
//        Switch(
//            isChecked.value,
//            onCheckedChange = {
//                prefs.edit { putBoolean(preferenceKey, it) }
//                onCheckedChange(it)
//            },
//            modifier = Modifier.clickable(false) {}
//        )
//    }
//}
//
//@Composable
//@Preview
//fun Preference(
//    preferenceTitle: String,
//    preferenceDescription: String? = null,
//    onClick: () -> Unit
//) {
//    ConstraintLayout(modifier = Modifier.padding(12.dp, 4.dp).clickable(onClick = onClick)) {
//        val (title, description, switch) = createRefs()
//        Text(preferenceTitle, fontSize = 16.sp, modifier = Modifier.constrainAs(title) {
//            top.linkTo(parent.top)
//            start.linkTo(parent.start)
//            end.linkTo(switch.start, 4.dp)
//            if (preferenceDescription != null) {
//                bottom.linkTo(description.top)
//            } else {
//                bottom.linkTo(parent.bottom)
//            }
//        })
//        if (preferenceDescription != null) {
//            Text(preferenceDescription, fontSize = 13.sp, modifier = Modifier.constrainAs(description) {
//                top.linkTo(title.bottom)
//            })
//        }
//    }
//
//}