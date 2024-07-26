package com.irostec.boardgamemanager.application.core;

import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.error.CreateBoardGameFromBGGException;
import com.irostec.boardgamemanager.application.core.shared.getcurrentuser.error.GetCurrentUserError;
import com.irostec.boardgamemanager.application.core.shared.includeboardgame.exception.IncludeBoardGameException;
import lombok.Getter;

public interface CreateAndIncludeBoardGameFromBGG {

    record Input(String externalId, String reasonForInclusion) {}

    record Output(long boardGameId, long boardGameInclusionId) {}

    abstract sealed class Failure
        extends Exception
        permits UserRetrievalException, BoardGameCreationException, BoardGameInclusionException
    {

        protected Failure(Throwable cause) {
            super(cause);
        }

        protected Failure(String message) {
            super(message);
        }

    }

    final class BoardGameCreationException extends Failure {

        public BoardGameCreationException(CreateBoardGameFromBGGException cause) {
            super(cause);
        }

    }

    @Getter
    final class UserRetrievalException extends Failure {

        private final GetCurrentUserError source;

        public UserRetrievalException(GetCurrentUserError source) {
            super("Couldn't get the current user");
            this.source = source;
        }

    }

    @Getter
    final class BoardGameInclusionException extends Failure {

        private final IncludeBoardGameException source;

        public BoardGameInclusionException(IncludeBoardGameException source) {
            super("Could not include the board game");
            this.source = source;
        }

    }

    CreateAndIncludeBoardGameFromBGG.Output execute(CreateAndIncludeBoardGameFromBGG.Input input)
        throws CreateAndIncludeBoardGameFromBGG.Failure;

}
