package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error;


public sealed class CreateBoardGameFromBGGException
    extends Exception
    permits MissingPropertyException, BoundaryException, BGGApiException
{
}
