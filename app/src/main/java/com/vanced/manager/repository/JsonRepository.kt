package com.vanced.manager.repository

import com.vanced.manager.domain.model.Json

interface JsonRepository {

    suspend fun fetch(): Json

}