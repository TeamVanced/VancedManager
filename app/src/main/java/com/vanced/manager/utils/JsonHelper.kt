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

    var dataMap: HashMap<String, JsonObject> = HashMap()

    suspend fun getJson(url: String): JsonObject
    {
        if(dataMap.containsKey(url))
        {
            return dataMap[url]!!
        }
        else
        {
            dataMap[url] = getSuspendJson(url)
            return dataMap[url]!!
        }
    }

    suspend fun getSuspendJson(url: String): com.beust.klaxon.JsonObject =
        Parser.default().parse(
            StringBuilder(url.httpGet().awaitString())
        ) as com.beust.klaxon.JsonObject

    suspend fun getJsonArray(url: String): JsonArray<String> =
        Klaxon().parseArray<String>(
            url.httpGet().awaitString()
        ) as JsonArray<String>

}