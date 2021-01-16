package com.vanced.manager.utils

import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.httpGet

private var dataMap: HashMap<String, JsonObject> = HashMap()

suspend fun getJson(url: String): JsonObject? {
    return try {
        if (dataMap.containsKey(url)) {
            dataMap[url]
        } else {
            dataMap[url] = getSuspendJson(url)
            dataMap[url]
        }
    } catch (e: Exception) {
        //This null is NEEDED, do NOT try to "fix" NPE here!!!
        null
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
