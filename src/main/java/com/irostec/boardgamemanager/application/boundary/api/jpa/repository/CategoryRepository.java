package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.stream.Stream;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Stream<Category> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

}
