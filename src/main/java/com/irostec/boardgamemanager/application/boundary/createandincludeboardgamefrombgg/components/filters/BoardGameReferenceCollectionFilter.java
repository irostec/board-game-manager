package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGameReference;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameReferenceRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import com.irostec.boardgamemanager.common.error.BoundaryException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGameReferenceCollectionFilter
    implements CollectionFilter<Long, String, BoardGameReference, BoundaryException>
{

    private final BoardGameReferenceRepository boardGameReferenceRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGameReference> findBySharedKeyAndUniqueKeysIn(
        Long sharedKey,
        Collection<String> uniqueKeys
    ) throws BoundaryException {

        return boardGameReferenceRepository.findByDataSourceIdAndExternalIdIn(sharedKey, uniqueKeys);

    }

}
