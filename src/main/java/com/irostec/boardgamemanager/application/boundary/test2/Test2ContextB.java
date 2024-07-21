package com.irostec.boardgamemanager.application.boundary.test2;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class Test2ContextB {

    private final Test2ContextC test2ContextC;

    public BoardGame execute() {

        return test2ContextC.createNewBoardGame();

    }

}
