package com.irostec.boardgamemanager.application.boundary.test1;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;

@Component
@AllArgsConstructor
public class Test1ContextB {

    private final Test1ContextC test1ContextC;

    public BoardGame execute() {

        test1ContextC.createNewBoardGame();

        throw new RuntimeException();

    }

}
