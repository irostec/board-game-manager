package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ImageType;
import com.irostec.boardgamemanager.application.boundary.api.jpa.helper.ImageTypeMapper;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ImageRepository;

import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.ImageCollectionFilter;
import com.irostec.boardgamemanager.common.error.BGMException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@Component
@AllArgsConstructor
public class ImageService {

    private static final Function<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, String> IMAGE_TO_KEY =
        com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image::link;

    private static final Function<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType> IMAGE_TO_IMAGE_TYPE =
        com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image::type;


    private final CacheService cacheService;
    private final ImageRepository imageRepository;
    private final ImageCollectionFilter imageCollectionFilter;
    private final EntityCollectionProcessor entityCollectionProcessor;

    private static Map<com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType, ImageType> buildImageTypeMap(
        CacheService cacheService,
        Collection<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image> images
    ) throws BGMException {

        Set<com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType> imageTypes =
            images.stream()
                .map(IMAGE_TO_IMAGE_TYPE)
                .collect(Collectors.toSet());

        Map<com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType, ImageType> imageTypeMap =
            new HashMap<>();

        for (com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType imageType : imageTypes) {

            imageTypeMap.put(
                imageType,
                cacheService.findImageTypeByName(ImageTypeMapper.INSTANCE.map(imageType))
            );

        }

        return imageTypeMap;

    }

    @Transactional(rollbackFor = Throwable.class)
    public Collection<Image> saveImages(
        Long boardGameReferenceId,
        Collection<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image> images
    ) throws BGMException {

        Map<com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType, ImageType> imageTypeMap =
            buildImageTypeMap(cacheService, images);

        EntityCollectionProcessor.PartialFunctionDefinition<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, Image> partialFunctionDefinition =
            entityCollectionProcessor.buildPartialFunctionDefinition(
                images, boardGameReferenceId, imageCollectionFilter, IMAGE_TO_KEY, Image::getUrl
            );

        Function<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, Image> imageMapping =
            input -> {

                ImageType imageType = imageTypeMap.get(IMAGE_TO_IMAGE_TYPE.apply(input));

                com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image image =
                    new com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image();
                image.setImageTypeId(imageType.getId());
                image.setBoardGameReferenceId(boardGameReferenceId);
                image.setUrl(input.link());

                return image;

            };

        Collection<Image> newImages = wrapWithErrorHandling(() ->
            imageRepository.saveAllAndFlush(
                partialFunctionDefinition.getUnmappedDomain().stream()
                    .map(imageMapping)
                    .toList()
            )
        );

        Collection<Image> existingImages = partialFunctionDefinition.getImage();

        return Stream.concat(newImages.stream(), existingImages.stream()).toList();

    }

}
