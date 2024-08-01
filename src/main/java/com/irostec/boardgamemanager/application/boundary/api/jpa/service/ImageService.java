package com.irostec.boardgamemanager.application.boundary.api.jpa.service;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ImageType;
import com.irostec.boardgamemanager.application.boundary.api.jpa.helper.ImageTypeMapper;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ImageRepository;

import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.EntityCollectionProcessor;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters.ImageCollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

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

    @Transactional
    public Collection<Image> saveImages(
        BoardGameReference boardGameReference,
        Collection<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image> images
    ) {

        EntityCollectionProcessor.Result<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, Image> filteringResult =
            entityCollectionProcessor.apply(
                images, boardGameReference, imageCollectionFilter, IMAGE_TO_KEY, Image::getUrl
            );

        BiConsumer<com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image, Image> imageConsumer =
            (bggImage, jpaImage) -> {

                ImageType imageType = cacheService.findImageTypeByName(
                    ImageTypeMapper.INSTANCE.map(
                        IMAGE_TO_IMAGE_TYPE.apply(bggImage)
                    )
                );

                jpaImage.setImageType(imageType);
                jpaImage.setBoardGameReference(boardGameReference);
                jpaImage.setUrl(bggImage.link());

            };

        Collection<Image> newImages = imageRepository.saveAll(
            filteringResult.getUnmapped().stream()
                .map(
                    bggImage -> {

                        Image newImage = new Image();
                        imageConsumer.accept(bggImage, newImage);

                        return newImage;

                    }
                )
                .toList()
        );

        filteringResult.getMappings().forEach(imageConsumer);

        return Stream.concat(newImages.stream(), filteringResult.getMappings().values().stream()).toList();

    }

}
