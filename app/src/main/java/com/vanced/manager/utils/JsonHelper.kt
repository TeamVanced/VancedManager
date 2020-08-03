package com.vanced.manager.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.httpGet

object JsonHelper {

    suspend fun getJson(url: String): JsonObject =
        Parser.default().parse(
            StringBuilder(url.httpGet().awaitString())
        ) as JsonObject

    suspend fun getJsonArray(url: String): JsonArray<*> =
        Parser.default().parse(
            StringBuilder(url.httpGet().awaitString())
        ) as JsonArray<*>

}