package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.caller;

import com.irostec.boardgamemanager.application.core.CreateAndIncludeBoardGameFromBGG;
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

    private final CreateAndIncludeBoardGameFromBGG createAndIncludeBoardGameFromBGG;

    @PostMapping(value = "/boardgamegeek/{externalId}")
    @HasUserRole
    ResponseEntity<?> createAndIncludeBoardGameFromBGG(
        @PathVariable String externalId,
        @Valid @RequestBody Request request
    ) {

        try {

            CreateAndIncludeBoardGameFromBGG.Output output =
                createAndIncludeBoardGameFromBGG.execute(
                    new CreateAndIncludeBoardGameFromBGG.Input(externalId, request.reasonForInclusion())
                );

            return ResponseEntity.ok(new Response(output.boardGameId(), output.boardGameInclusionId()));

        }
        catch (CreateAndIncludeBoardGameFromBGG.Failure failure) {
            return exceptionToHttpResponse(failure);
        }

    }

    private static ResponseEntity<String> exceptionToHttpResponse(CreateAndIncludeBoardGameFromBGG.Failure failure) {

        return switch (failure) {
            case CreateAndIncludeBoardGameFromBGG.UserRetrievalException userRetrievalException -> {
                final String userRetrievalErrorMessage =
                    "Couldn't retrieve the information associated with the current user";

                yield new ResponseEntity<>(userRetrievalErrorMessage, HttpStatus.NOT_FOUND);
            }
            case CreateAndIncludeBoardGameFromBGG.BoardGameCreationException boardGameCreationException -> {
                final String boardGameCreationErrorMessage =
                    "An error occurred while attempting to create the board game. Please try again";

                yield new ResponseEntity<>(boardGameCreationErrorMessage, HttpStatus.BAD_REQUEST);
            }
            case CreateAndIncludeBoardGameFromBGG.BoardGameInclusionException boardGameInclusionException -> {
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
