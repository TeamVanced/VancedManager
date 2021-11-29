package com.vanced.manager.repository

import com.vanced.manager.domain.model.Data

interface DataRepository {

    suspend fun fetch(): Data

}