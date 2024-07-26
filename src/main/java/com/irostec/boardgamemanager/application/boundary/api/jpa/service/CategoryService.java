package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameCategory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameCategoryRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.CategoryRepository;

import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.BoardGameCategoryCollectionFilter;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.CategoryCollectionFilter;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.error.BoundaryException;

import java.util.Collection;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;
    private final CategoryCollectionFilter categoryCollectionFilter;
    private final BoardGameCategoryCollectionFilter boardGameCategoryCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private Collection<Category> saveCategories(Long dataSourceId, Collection<Link> links)
    throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Link, Category> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                links, dataSourceId, categoryCollectionFilter, Link::id, Category::getExternalId
            );

        Collection<Category> newCategories = wrapWithErrorHandling(() ->
            categoryRepository.saveAllAndFlush(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        link -> {

                            Category category = new Category();
                            category.setDataSourceId(dataSourceId);
                            category.setExternalId(link.id());
                            category.setName(link.value());

                            return category;

                        }
                    )
                    .toList()
            )
        );

        Collection<Category> existingCategories = partialFunctionDefinition.getImage();

        return Stream.concat(newCategories.stream(), existingCategories.stream()).toList();

    }

    private Collection<BoardGameCategory> saveBoardGameCategories(Long boardGameId, Collection<Category> categories)
    throws BoundaryException {

        EntityCollectionProcessor.PartialFunctionDefinition<Category, BoardGameCategory> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                    categories, boardGameId, boardGameCategoryCollectionFilter, Category::getId, BoardGameCategory::getCategoryId
                );

        Collection<BoardGameCategory> newBoardGameCategories = wrapWithErrorHandling(() ->
            boardGameCategoryRepository.saveAllAndFlush(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(
                        category -> {

                            BoardGameCategory boardGameCategory = new BoardGameCategory();
                            boardGameCategory.setBoardGameId(boardGameId);
                            boardGameCategory.setCategoryId(category.getId());

                            return boardGameCategory;

                        }
                    )
                    .toList()
            )
        );

        Collection<BoardGameCategory> existingBoardGameCategories = partialFunctionDefinition.getImage();

        return Stream.concat(newBoardGameCategories.stream(), existingBoardGameCategories.stream()).toList();

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<BoardGameCategory> saveBoardGameCategories(
        Long boardGameId,
        Long dataSourceId,
        Collection<Link> links
    ) throws BoundaryException {

        Collection<Category> categories = this.saveCategories(dataSourceId, links);

        return saveBoardGameCategories(boardGameId, categories);

    }

}
