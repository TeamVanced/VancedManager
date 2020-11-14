package com.vanced.manager.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.httpGet

object JsonHelper {

    private var dataMap: HashMap<String, JsonObject> = HashMap()

    suspend fun getJson(url: String): JsonObject? {
        return if (dataMap.containsKey(url)) {
            dataMap[url]
        } else {
            dataMap[url] = getSuspendJson(url)
            dataMap[url]
        }
    }

    private suspend fun getSuspendJson(url: String): JsonObject =
        Parser.default().parse(
            StringBuilder(url.httpGet().awaitString())
        ) as JsonObject

    suspend fun getJsonArray(url: String): JsonArray<String> =
        Klaxon().parseArray<String>(
            url.httpGet().awaitString()
        ) as JsonArray<String>

}