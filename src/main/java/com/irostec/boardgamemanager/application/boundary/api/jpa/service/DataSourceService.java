package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.DataSourceName;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class DataSourceService {

    private final CacheService cacheService;

    public <E> Either<E, DataSource> getDataSource(
        DataSourceName dataSourceName,
        Function<Throwable, E> exceptionToError
    ) {

        return wrapWithErrorHandling(
            () -> this.cacheService.findDataSourceByName(dataSourceName),
                exceptionToError
        );

    }

}
