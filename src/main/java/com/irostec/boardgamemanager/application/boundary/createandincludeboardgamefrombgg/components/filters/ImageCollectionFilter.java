package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ImageRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

import static com.irostec.boardgamemanager.common.utility.Functions.wrapWithErrorHandling;

@AllArgsConstructor
@Component
public class ImageCollectionFilter
    implements CollectionFilter<Long, String, Image, BoundaryException>
{

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<Image> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return wrapWithErrorHandling(
            () ->imageRepository.findByBoardGameReferenceIdAndUrlIn(sharedKey, uniqueKeys)
        );

    }

}
