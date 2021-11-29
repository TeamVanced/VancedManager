package com.vanced.manager.repository

import com.vanced.manager.network.DataService
import com.vanced.manager.network.model.DataDtoMapper

class MainRepository(
    private val mainService: DataService,
    private val mapper: DataDtoMapper
) : DataRepository {

    override suspend fun fetch() =
        mapper.mapToModel(mainService.get())

}