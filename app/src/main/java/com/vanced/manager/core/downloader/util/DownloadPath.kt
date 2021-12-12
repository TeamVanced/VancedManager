package com.vanced.manager.core.downloader.util

import android.content.Context

fun getVancedYoutubePath(
    version: String,
    variant: String,
    context: Context
) = context.getExternalFilesDir("vanced_youtube")!!.path + "/$version/$variant"

fun getStockYoutubePath(
    version: String,
    context: Context
) = context.getExternalFilesDir("stock_youtube")!!.path + "/$version"

fun getVancedMusicPath(
    version: String,
    variant: String,
    context: Context
) = context.getExternalFilesDir("vanced_music")!!.path + "/$version/$variant"

fun getMicrogPath(
    context: Context
) = context.getExternalFilesDir("microg")!!.path