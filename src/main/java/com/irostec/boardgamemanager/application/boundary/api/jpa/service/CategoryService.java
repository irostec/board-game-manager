package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameCategory;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Category;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameCategoryRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.CategoryRepository;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.common.utility.PartialFunctionDefinition;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static com.irostec.boardgamemanager.common.utility.Functions.zipper;
import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;
import static com.irostec.boardgamemanager.common.utility.Functions.processItems;
import static com.irostec.boardgamemanager.common.utility.Functions.mapAndProcess;

@Component
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final BoardGameCategoryRepository boardGameCategoryRepository;

    public <E> Either<E, Collection<BoardGameCategory>> saveCategories(
            Long boardGameId,
            Long dataSourceId,
            Supplier<Collection<Link>> linksSupplier,
            Function<Link, String> linkToKey,
            Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<Link, Category>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                linksSupplier.get(),
                links -> wrapWithErrorHandling(
                    () -> categoryRepository.findByDataSourceIdAndExternalIdIn(
                        dataSourceId,
                        links.stream().map(linkToKey).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                linkToKey,
                Category::getExternalId
            );

        Either<E, Collection<Category>> categoriesContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        link -> {

                            Category category = new Category();
                            category.setDataSourceId(dataSourceId);
                            category.setExternalId(link.id());
                            category.setName(link.value());

                            return category;

                        },
                        categoryRepository::saveAll,
                        exceptionToError
                    ),
                    Either::right,
                    zipper((newInputs, newItems) -> newItems),
                    zipper((existingInputs, existingItems) -> existingItems),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        Either<E, Collection<BoardGameCategory>> boardGameCategoriesContainer =
            categoriesContainer.flatMap(
                categories -> mapAndProcess(
                    categories,
                    category -> {

                        BoardGameCategory boardGameCategory = new BoardGameCategory();
                        boardGameCategory.setBoardGameId(boardGameId);
                        boardGameCategory.setCategoryId(category.getId());

                        return boardGameCategory;

                    },
                    boardGameCategoryRepository::saveAll,
                    exceptionToError
                )
            );

        return boardGameCategoriesContainer;

    }

}
