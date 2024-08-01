package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.ImageRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class ImageCollectionFilter
    implements CollectionFilter<BoardGameReference, String, Image>
{

    private final ImageRepository imageRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<Image> findByParentAndUniqueKeysIn(
        BoardGameReference parent,
        Collection<String> uniqueKeys
    ) {

        return imageRepository.findByBoardGameReferenceAndUrlIn(parent, uniqueKeys);

    }

}
