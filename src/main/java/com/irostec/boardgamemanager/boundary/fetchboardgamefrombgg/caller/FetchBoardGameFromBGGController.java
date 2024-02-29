package com.irostec.boardgamemanager.boundary.fetchboardgamefrombgg.caller;

import com.irostec.boardgamemanager.application.FetchBoardGameFromBGG;

import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.common.exception.ExternalServerCallException;
import com.irostec.boardgamemanager.common.exception.ExternalServerHttpException;
import com.irostec.boardgamemanager.common.exception.NotFoundException;
import com.irostec.boardgamemanager.common.utility.LoggingUtils;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * FetchBoardGameFromBGGController
 * A web interface for the FetchBoardGameFromBGG use case
 */
@RestController
@RequestMapping("/v1/external-services")
@AllArgsConstructor
class FetchBoardGameFromBGGController {

    private static final String METHOD_NAME = "fetchBoardGameFromBGG";

    private final Logger logger = LogManager.getLogger(FetchBoardGameFromBGGController.class);

    private final FetchBoardGameFromBGG fetchBoardGameFromBGG;

    @GetMapping(value = "/boardgamegeek/{externalId}")
    Map<String, Object> fetchBoardGameFromBGG(@PathVariable String externalId)
            throws BGMException {

        return OutputMapper.toMap(fetchBoardGameFromBGG.execute(externalId));

    }

    @ExceptionHandler(NotFoundException.class)
    ResponseEntity<String> handle(NotFoundException ex) {

        LoggingUtils.error(logger, METHOD_NAME, ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({ExternalServerCallException.class, ExternalServerHttpException.class})
    ResponseEntity<String> handle(BGMException ex) {

        LoggingUtils.error(logger, METHOD_NAME, ex);

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_GATEWAY);

    }

}
