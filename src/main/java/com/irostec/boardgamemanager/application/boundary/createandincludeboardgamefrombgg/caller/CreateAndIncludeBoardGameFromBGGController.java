package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.caller;

import com.irostec.boardgamemanager.application.core.CreateAndIncludeBoardGameFromBGGService;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.BoardGameCreationError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.BoardGameInclusionError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.CreateAndIncludeBoardGameFromBGGError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.error.UserRetrievalError;
import com.irostec.boardgamemanager.application.core.createandincludeboardgamefrombgg.input.RequestToCreateAndIncludeBoardGameFromBGG;
import com.irostec.boardgamemanager.configuration.security.annotation.HasUserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/external-services")
@AllArgsConstructor
public class CreateAndIncludeBoardGameFromBGGController {

    private final CreateAndIncludeBoardGameFromBGGService createAndIncludeBoardGameFromBGGService;

    @PostMapping(value = "/boardgamegeek/{externalId}")
    @HasUserRole
    ResponseEntity<?> createAndIncludeBoardGameFromBGG(
        @PathVariable String externalId,
        @Valid @RequestBody Request request
    ) {

        return
            createAndIncludeBoardGameFromBGGService.execute(
                new RequestToCreateAndIncludeBoardGameFromBGG(externalId, request.reasonForInclusion())
            )
            .map(createAndIncludeBoardGameFromBGGServiceResult -> new Response(createAndIncludeBoardGameFromBGGServiceResult.boardGameId(), createAndIncludeBoardGameFromBGGServiceResult.boardGameInclusionId()))
            .fold(CreateAndIncludeBoardGameFromBGGController::errorToHttpResponse, ResponseEntity::ok);

    }

    private static ResponseEntity<String> errorToHttpResponse(CreateAndIncludeBoardGameFromBGGError error) {

        return switch (error) {
            case UserRetrievalError userRetrievalError -> {
                final String userRetrievalErrorMessage =
                    "Couldn't retrieve the information associated with the current user";

                yield new ResponseEntity<>(userRetrievalErrorMessage, HttpStatus.NOT_FOUND);
            }
            case BoardGameCreationError boardGameCreationError -> {
                final String boardGameCreationErrorMessage =
                    "An error occurred while attempting to create the board game. Please try again";

                yield new ResponseEntity<>(boardGameCreationErrorMessage, HttpStatus.BAD_REQUEST);
            }
            case BoardGameInclusionError boardGameInclusionError -> {
                final String boardGameInclusionErrorMessage =
                    "An error occurred while attempting to include the board game in the user's list. Please try again";

                yield new ResponseEntity<>(boardGameInclusionErrorMessage, HttpStatus.BAD_REQUEST);
            }
        };

    }

    record Request(
        @NotBlank(message = "The justification for the inclusion of the board game is mandatory") String reasonForInclusion) {
    }

    record Response(long boardGameId, long boardGameInclusionId) {}

}
