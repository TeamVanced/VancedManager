package com.vanced.manager.domain.util

interface EntityMapper<T, Model> {

    suspend fun mapToModel(entity: T): Model

}