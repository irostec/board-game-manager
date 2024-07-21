package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ImageType;
import com.irostec.boardgamemanager.application.boundary.api.jpa.helper.ImageTypeMapper;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ImageRepository;

import com.irostec.boardgamemanager.common.utility.PartialFunctionDefinition;
import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.*;

@Component
@AllArgsConstructor
public class ImageService {

    private static Function<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, String> IMAGE_TO_KEY =
        com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image::link;

    private final CacheService cacheService;
    private final ImageRepository imageRepository;

    public <E> Either<E, Collection<Image>> saveImages(
        Long boardGameReferenceId,
        Supplier<Collection<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image>> imagesSupplier,
        Function<Throwable, E> exceptionToError
    ) {

        Either<E, PartialFunctionDefinition<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, Image>> partialFunctionDefinitionContainer =
            PartialFunctionDefinition.of(
                imagesSupplier.get(),
                images -> wrapWithErrorHandling(
                    () -> imageRepository.findByBoardGameReferenceIdAndUrlIn(
                            boardGameReferenceId,
                            images.stream().map(IMAGE_TO_KEY).collect(Collectors.toList())
                    ),
                    exceptionToError
                ),
                IMAGE_TO_KEY,
                Image::getUrl
            );

        Either<E, Collection<Image>> imagesContainer =
            partialFunctionDefinitionContainer.flatMap(partialFunctionDefinition ->
                processItems(
                    partialFunctionDefinition,
                    newInputs -> mapAndProcess(
                        newInputs,
                        input -> {

                            ImageType imageType = this.cacheService.findImageTypeByName(ImageTypeMapper.INSTANCE.map(input.type()));

                            com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image image =
                                new com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image();
                            image.setImageTypeId(imageType.getId());
                            image.setBoardGameReferenceId(boardGameReferenceId);
                            image.setUrl(input.link());

                            return image;

                        },
                        imageRepository::saveAll,
                        exceptionToError
                    ),
                    Either::right,
                    zipper((newInputs, newItems) -> newItems),
                    zipper((existingInputs, existingItems) -> existingItems),
                    (newItems, existingItems) ->
                        Stream.concat(newItems.stream(), existingItems.stream()).collect(Collectors.toList())
                )
            );

        return imagesContainer;

    }

}
