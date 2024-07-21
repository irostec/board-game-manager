package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ImageType;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.ImageTypeName;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ImageTypeRepository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.DataSourceRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.cache.annotation.Cacheable;

@Component
@AllArgsConstructor
public final class CacheService {

    private final DataSourceRepository dataSourceRepository;
    private final ImageTypeRepository imageTypeRepository;

    @Cacheable("dataSources")
    public DataSource findDataSourceByName(DataSourceName name) {

        return this.dataSourceRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Data source not found: " + name.name()));

    }

    @Cacheable("imageTypes")
    public ImageType findImageTypeByName(ImageTypeName name) {

        return this.imageTypeRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Image type not found: " + name.name()));

    }

}
