package com.irostec.boardgamemanager.application.boundary.test1;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameRepository;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;

import java.util.List;

@Component
@AllArgsConstructor
public class Test1ContextA {

    private final Test1ContextB test1ContextB;
    private final BoardGameRepository boardGameRepository;

    @Transactional
    public boolean execute() {

        try {
            test1ContextB.execute();
        } catch (Exception ex) {
            List<BoardGame> boardGames = boardGameRepository.findAll();
            return boardGames.isEmpty();
        }

        return false;

    }

}
