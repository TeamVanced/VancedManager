package com.vanced.manager.ui.util

import androidx.compose.ui.unit.dp

val defaultContentPaddingHorizontal = 16.dp
val defaultContentPaddingVertical = 12.dp

fun test(anotherFun: () -> Int) = anotherFun() + 5
fun test(anotherNum: Int) = anotherNum + 5

fun test2() {
    test {
        print("haha jonathan you are banging my number")
        return@test 0
    }
    test(0)
}