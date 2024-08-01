package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ImageType;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.ImageTypeName;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ImageTypeRepository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DataSourceRepository;

import com.irostec.boardgamemanager.common.error.RequiredValueNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

@Component
@AllArgsConstructor
public final class CacheService {

    private final DataSourceRepository dataSourceRepository;
    private final ImageTypeRepository imageTypeRepository;

    @Cacheable("dataSources")
    public DataSource findDataSourceByName(DataSourceName name) {

        Optional<DataSource> optionalDataSource = this.dataSourceRepository.findByName(name);

        return optionalDataSource
                .orElseThrow(() -> new RequiredValueNotFoundException("Data source not found: " + name.name()));

    }

    @Cacheable("imageTypes")
    public ImageType findImageTypeByName(ImageTypeName name) {

        Optional<ImageType> optionalImageType = this.imageTypeRepository.findByName(name);

        return optionalImageType
            .orElseThrow(() -> new RequiredValueNotFoundException("Image type not found: " + name.name()));

    }

}
