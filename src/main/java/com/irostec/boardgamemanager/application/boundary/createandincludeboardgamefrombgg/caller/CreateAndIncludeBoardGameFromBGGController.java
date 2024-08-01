package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg.caller;

import com.irostec.boardgamemanager.application.core.CreateAndIncludeBoardGameFromBGG;
import com.irostec.boardgamemanager.configuration.security.annotation.HasUserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/external-services")
@AllArgsConstructor
public class CreateAndIncludeBoardGameFromBGGController {

    private final Logger logger = LogManager.getLogger(CreateAndIncludeBoardGameFromBGGController.class);

    private final CreateAndIncludeBoardGameFromBGG createAndIncludeBoardGameFromBGG;

    @PostMapping(value = "/boardgamegeek/{externalId}")
    @HasUserRole
    ResponseEntity<?> createAndIncludeBoardGameFromBGG(
        @PathVariable String externalId,
        @Valid @RequestBody Request request
    ) {

        try {

            CreateAndIncludeBoardGameFromBGG.Input input =
                    new CreateAndIncludeBoardGameFromBGG.Input(externalId, request.reasonForInclusion());

            logger.info(
                String.format(
                    "Executing use case to create and include a board game from boardgamegeek.com with the following input: %s",
                    input
                )
            );

            CreateAndIncludeBoardGameFromBGG.Output output = createAndIncludeBoardGameFromBGG.execute(input);

            return ResponseEntity.ok(new Response(output.boardGameId(), output.boardGameInclusionId()));

        }
        catch (Exception exception) {
            return exceptionToHttpResponse(exception);
        }

    }

    private ResponseEntity<String> exceptionToHttpResponse(Exception exception) {

        logger.error("Error executing use case to create and include a board game from boardgamegeek.com", exception);

        final String message = "An error occurred while attempting to create the board game. Please try again";

        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);

    }

    record Request(
        @NotBlank(message = "The justification for the inclusion of the board game is mandatory") String reasonForInclusion) {
    }

    record Response(long boardGameId, long boardGameInclusionId) {}

}
