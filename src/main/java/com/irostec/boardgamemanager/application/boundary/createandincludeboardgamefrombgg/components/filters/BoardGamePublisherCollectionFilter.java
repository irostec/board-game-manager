package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.components.filters;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGamePublisher;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Publisher;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGamePublisherRepository;
import com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.types.CollectionFilter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;

@AllArgsConstructor
@Component
public class BoardGamePublisherCollectionFilter
    implements CollectionFilter<BoardGame, Publisher, BoardGamePublisher>
{

    private final BoardGamePublisherRepository boardGameDesignerRepository;

    @Transactional(readOnly = true)
    @Override
    public Stream<BoardGamePublisher> findByParentAndUniqueKeysIn(
        BoardGame parent,
        Collection<Publisher> uniqueKeys
    ) {

        return boardGameDesignerRepository.findByBoardGameAndPublisherIn(parent, uniqueKeys);

    }

}
