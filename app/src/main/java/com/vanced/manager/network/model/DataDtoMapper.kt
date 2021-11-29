package com.vanced.manager.network.model

import com.vanced.manager.core.preferences.holder.managerVariantPref
import com.vanced.manager.domain.model.Data
import com.vanced.manager.domain.util.EntityMapper

class DataDtoMapper(
    private val appDtoMapper: AppDtoMapper
) : EntityMapper<DataDto, Data> {

    override suspend fun mapToModel(
        entity: DataDto
    ) = with(entity) {
        Data(
            manager = appDtoMapper.mapToModel(manager),
            apps =
                if (managerVariantPref == "root") {
                    apps.root
                } else {
                    apps.nonroot
                }.map { appDto ->
                    appDtoMapper.mapToModel(appDto)
                }
        )
    }
}