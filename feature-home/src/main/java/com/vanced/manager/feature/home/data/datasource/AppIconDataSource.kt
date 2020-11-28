package com.vanced.manager.feature.home.data.datasource

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.vanced.manager.feature.home.data.api.AppsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AppIconDataSource {

    suspend fun getIcon(url: String): Bitmap
}

class AppIconDataSourceImpl(
    private val api: AppsApi
) : AppIconDataSource {

    override suspend fun getIcon(url: String): Bitmap =
        withContext(Dispatchers.IO) {
            val inputStream = api.getIcon(url).byteStream()
            BitmapFactory.decodeStream(inputStream)
        }
}