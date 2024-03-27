package com.irostec.boardgamemanager.boundary.getboardgamefrombgg.caller;

import com.irostec.boardgamemanager.application.GetBoardGameFromBGGService;

import com.irostec.boardgamemanager.application.shared.bggapi.error.BGGApiError;
import com.irostec.boardgamemanager.application.shared.bggapi.error.BoardGameNotFound;
import com.irostec.boardgamemanager.application.shared.bggapi.error.ExternalServiceFailure;
import com.irostec.boardgamemanager.application.shared.bggapi.error.InvalidBoardGameData;
import com.irostec.boardgamemanager.common.error.NetworkFailure;
import com.irostec.boardgamemanager.common.error.UnsuccessfulResponse;
import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import com.irostec.boardgamemanager.configuration.security.core.annotation.HasUserRole;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final String METHOD_NAME = "getBoardGameFromBGG";

    private final Logger logger = LogManager.getLogger(GetBoardGameFromBGGController.class);

    private final GetBoardGameFromBGGService getBoardGameFromBGGService;

    @GetMapping(value = "/boardgamegeek/{externalId}")
    @HasUserRole
    ResponseEntity<?> getBoardGameFromBGG(@PathVariable String externalId) {

        return getBoardGameFromBGGService.execute(externalId)
                .map(OutputMapper::toMap)
                .peekLeft(this::logError)
                .fold(GetBoardGameFromBGGController::errorToHttpResponse, ResponseEntity::ok);

    }


    private void logError(BGGApiError error) {

        switch (error) {
            case BoardGameNotFound boardGameNotFound:
                ;
                break;
            case ExternalServiceFailure externalServiceFailure:
                switch(externalServiceFailure.error()) {
                    case NetworkFailure networkFailure:
                        final String networkFailureMessage =
                                String.format("Encountered a network error while trying to request data from %s using the external id '%s': ",
                                        EXTERNAL_API_NAME,
                                        externalServiceFailure.externalId()
                                );
                        LoggingUtils.error(logger, METHOD_NAME, networkFailureMessage, networkFailure.source());
                        break;
                    case UnsuccessfulResponse unsuccessfulResponse:
                        final String unsuccessfulResponseMessage =
                                String.format("Received an unexpected response while trying to request data from %s using the external id '%s'. Message: %s. HTTP status: %s",
                                        EXTERNAL_API_NAME,
                                        externalServiceFailure.externalId(),
                                        unsuccessfulResponse.message(),
                                        unsuccessfulResponse.httpCode()
                                );
                        LoggingUtils.error(logger, METHOD_NAME, unsuccessfulResponseMessage);
                        break;
                };
                break;
            case InvalidBoardGameData invalidBoardGameData:
                final String invalidBoardGameDataMessage = String.format("The following errors were found while parsing the response received from %s using the external id '%s': %s",
                        EXTERNAL_API_NAME,
                        invalidBoardGameData.externalId(),
                        invalidBoardGameData.errorMessagesByPropertyName()
                );
                LoggingUtils.error(logger, METHOD_NAME, invalidBoardGameDataMessage);
                break;
        }

    }

    private static ResponseEntity<String> errorToHttpResponse(BGGApiError error) {

        return switch (error) {
            case BoardGameNotFound boardGameNotFound -> {
                final String boardGameNotFoundMessage = String.format("Couldn't find a board game with id '%s'",
                        boardGameNotFound.externalId()
                );
                yield new ResponseEntity<>(boardGameNotFoundMessage, HttpStatus.NOT_FOUND);
            }
            case ExternalServiceFailure externalServiceFailure -> {
                final String externalServiceFailureMessage = String.format("An error occurred while trying to communicate with the %s API",
                        EXTERNAL_API_NAME
                );
                yield new ResponseEntity<>(externalServiceFailureMessage, HttpStatus.BAD_GATEWAY);
            }
            case InvalidBoardGameData invalidBoardGameData -> {
                final String invalidBoardGameDataMessage = String.format("The board game came with an invalid format from the %s api, and it cannot be parsed",
                        EXTERNAL_API_NAME
                );
                yield new ResponseEntity<>(invalidBoardGameDataMessage, HttpStatus.CONFLICT);
            }
        };

    }

}
