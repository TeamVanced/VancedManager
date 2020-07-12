package com.vanced.manager.utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.github.kittinunf.fuel.coroutines.awaitString
import com.github.kittinunf.fuel.httpGet

object JsonHelper {

    suspend fun getJson(url: String): JsonObject {
        val result = url.httpGet().awaitString()

        val parser: Parser = Parser.default()
        val stringBuilder: StringBuilder = StringBuilder(result)

        return parser.parse(stringBuilder) as JsonObject
    }

}