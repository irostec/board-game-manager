package com.irostec.boardgamemanager.application.boundary.test2;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class Test2ContextC {

    private final BoardGameRepository boardGameRepository;

    @Transactional
    public BoardGame createNewBoardGame(){

        BoardGame boardGame = new BoardGame();
        boardGame.setName("My latest obsession");

        return boardGameRepository.save(boardGame);

    }

}
