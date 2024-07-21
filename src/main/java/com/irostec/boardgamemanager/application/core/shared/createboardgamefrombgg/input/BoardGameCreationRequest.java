package com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.input;

public record BoardGameCreationRequest(
    String externalId,
    long userId
) {}
