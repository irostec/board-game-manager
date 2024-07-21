package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;

public record DatabaseError(Throwable cause) implements CreateBoardGameFromBGGError {
}
