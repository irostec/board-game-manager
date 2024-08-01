package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;

import com.irostec.boardgamemanager.common.error.BGMException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataSourceService {

    private final CacheService cacheService;

    public DataSource getDataSource(DataSourceName dataSourceName) {

        return this.cacheService.findDataSourceByName(dataSourceName);

    }

}
