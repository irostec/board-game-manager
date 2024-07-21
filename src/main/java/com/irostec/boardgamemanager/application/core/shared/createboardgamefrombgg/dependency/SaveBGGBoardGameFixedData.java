package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency;

import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGError;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.output.BoardGameSummary;
import com.irostec.boardgamemanager.application.core.shared.bggapi.output.BoardGameFromBGG;
import io.vavr.control.Either;

public interface SaveBGGBoardGameFixedData {

    Either<CreateBoardGameFromBGGError, BoardGameSummary> execute(BoardGameFromBGG boardGameFromBGG);

}
