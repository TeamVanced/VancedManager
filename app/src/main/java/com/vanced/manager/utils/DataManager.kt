package com.vanced.manager.utils

import com.beust.klaxon.Json
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.httpGet

object DataManager {

    var dataMap: HashMap<String, JsonObject> = HashMap()

    suspend fun getCheckJson(url: String): com.beust.klaxon.JsonObject {
        if(dataMap.containsKey(url))
        {
            return dataMap[url]!!
        }
        else
        {
            dataMap[url] = getJson(url)
            return dataMap[url]!!
        }
    }

    suspend fun getJson(url: String): com.beust.klaxon.JsonObject =
        Parser.default().parse(
            StringBuilder(url.httpGet().awaitString())
        ) as com.beust.klaxon.JsonObject

}