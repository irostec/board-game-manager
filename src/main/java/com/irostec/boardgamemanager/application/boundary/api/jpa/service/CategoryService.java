package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.*;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameCategoryRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.CategoryRepository;

import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameCategoryCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.CategoryCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;
    private final CategoryCollectionFilter categoryCollectionFilter;
    private final BoardGameCategoryCollectionFilter boardGameCategoryCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<Category> saveCategories(DataSource dataSource, Collection<Link> links) {

        EntityCollectionProcessor.Result<Link, Category> filteringResult =
            entityCollectionProcessor.apply(
                links, dataSource, categoryCollectionFilter, Link::id, Category::getExternalId
            );

        BiConsumer<Link, Category> categoryConsumer = (link, category) -> {

            category.setDataSource(dataSource);
            category.setExternalId(link.id());
            category.setName(link.value());

        };

        Collection<Category> newCategories = categoryRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    link -> {

                            Category newCategory = new Category();
                            categoryConsumer.accept(link, newCategory);

                            return newCategory;

                        }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(categoryConsumer);

        return Stream.concat(newCategories.stream(), filteringResult.getMappings().values().stream()).toList();

    }

    private Collection<BoardGameCategory> saveBoardGameCategories(
        BoardGame boardGame,
        Collection<Category> categories
    ) {

        EntityCollectionProcessor.Result<Category, BoardGameCategory> filteringResult =
            entityCollectionProcessor.apply(
                categories,
                boardGame,
                boardGameCategoryCollectionFilter,
                Function.identity(),
                BoardGameCategory::getCategory
            );

        BiConsumer<Category, BoardGameCategory> categoryConsumer =
            (category, boardGameCategory) -> {

                boardGameCategory.setBoardGame(boardGame);
                boardGameCategory.setCategory(category);

            };

        Collection<BoardGameCategory> newBoardGameCategories = boardGameCategoryRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    category -> {

                        BoardGameCategory newBoardGameCategory = new BoardGameCategory();
                        categoryConsumer.accept(category, newBoardGameCategory);

                        return newBoardGameCategory;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(categoryConsumer);

        return Stream.concat(newBoardGameCategories.stream(), filteringResult.getMappings().values().stream()).toList();

    }

    @Transactional
    public Collection<BoardGameCategory> saveBoardGameCategories(
        BoardGame boardGame,
        DataSource dataSource,
        Collection<Link> links
    ) {

        Collection<Category> categories = this.saveCategories(dataSource, links);

        return saveBoardGameCategories(boardGame, categories);

    }

}
