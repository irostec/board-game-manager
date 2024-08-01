package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Stream<Category> findByDataSourceAndExternalIdIn(DataSource dataSource, Collection<String> externalIds);

}
