package com.vanced.manager.repository

import com.vanced.manager.domain.model.Json
import com.vanced.manager.network.JsonService
import com.vanced.manager.network.model.JsonDtoMapper

class JsonRepositoryImpl(
    private val service: JsonService,
    private val mapper: JsonDtoMapper
) : JsonRepository {

    override suspend fun fetch(): Json {
        return mapper.mapToModel(service.get())
    }

}