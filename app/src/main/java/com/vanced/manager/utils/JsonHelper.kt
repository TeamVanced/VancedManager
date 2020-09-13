package com.vanced.manager.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object JsonHelper {

    suspend fun getJson(url: String): JsonObject
    {
        return DataManager.getCheckJson(url)
    }

    suspend fun getJsonArray(url: String): JsonArray<String> =
        Klaxon().parseArray<String>(
            url.httpGet().awaitString()
        ) as JsonArray<String>

}