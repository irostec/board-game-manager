package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency;

import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;

public interface SaveBGGBoardGameFixedData {

    BoardGameSummary execute(BoardGameFromBGG boardGameFromBGG);

}
