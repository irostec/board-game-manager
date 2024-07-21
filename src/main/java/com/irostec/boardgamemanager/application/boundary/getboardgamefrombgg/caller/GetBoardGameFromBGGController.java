package com.irostec.boardgamemanager.application.boundary.getboardgamefrombgg.caller;

import com.irostec.boardgamemanager.application.core.GetBoardGameFromBGGService;

import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BGGApiError;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.BoardGameNotFound;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.ExternalServiceFailure;
import com.irostec.boardgamemanager.application.core.shared.bggapi.error.InvalidBoardGameData;
import com.irostec.boardgamemanager.configuration.security.annotation.HasUserRole;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GetBoardGameFromBGGController
 * A web interface for the GetBoardGameFromBGG use case
 */
@RestController
@RequestMapping("/v1/external-services")
@AllArgsConstructor
class GetBoardGameFromBGGController {

    private static final String EXTERNAL_API_NAME = "boardgamegeek.com";

    private final GetBoardGameFromBGGService getBoardGameFromBGGService;

    @GetMapping(value = "/boardgamegeek/{externalId}")
    @HasUserRole
    ResponseEntity<?> getBoardGameFromBGG(@PathVariable String externalId) {

        return getBoardGameFromBGGService.execute(externalId)
            .map(OutputMapper::toMap)
            .fold(GetBoardGameFromBGGController::errorToHttpResponse, ResponseEntity::ok);

    }

    private static ResponseEntity<String> errorToHttpResponse(BGGApiError error) {

        return switch (error) {
            case BoardGameNotFound boardGameNotFound -> {
                final String boardGameNotFoundMessage =
                    String.format("Couldn't find a board game with id '%s'", boardGameNotFound.externalId());

                yield new ResponseEntity<>(boardGameNotFoundMessage, HttpStatus.NOT_FOUND);
            }
            case ExternalServiceFailure externalServiceFailure -> {
                final String externalServiceFailureMessage =
                    String.format("An error occurred while trying to communicate with the %s API", EXTERNAL_API_NAME);

                yield new ResponseEntity<>(externalServiceFailureMessage, HttpStatus.BAD_GATEWAY);
            }
            case InvalidBoardGameData invalidBoardGameData -> {
                final String invalidBoardGameDataMessage =
                    String.format("The board game came with an invalid format from the %s api, and it cannot be parsed", EXTERNAL_API_NAME);

                yield new ResponseEntity<>(invalidBoardGameDataMessage, HttpStatus.CONFLICT);
            }
        };

    }

}
