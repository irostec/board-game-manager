package com.irostec.boardgamemanager.boundary.fetchboardgamefrombgg;

import com.irostec.boardgamemanager.application.FetchBoardGameFromBGG;
import com.irostec.boardgamemanager.application.shared.BGGApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FetchBoardGameFromBGGConfiguration
 * Creates the beans needed for the FetchBoardGameFromBGG use case
 */
@Configuration
class FetchBoardGameFromBGGConfiguration {

    @Bean
    public FetchBoardGameFromBGG boardGameExternalQuery(BGGApi bggApi) {
        return new FetchBoardGameFromBGG(bggApi);
    }

}
