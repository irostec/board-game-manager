package com.irostec.boardgamemanager.boundary.getboardgamefrombgg;

import com.irostec.boardgamemanager.application.GetBoardGameFromBGGService;
import com.irostec.boardgamemanager.application.shared.BGGApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GetBoardGameFromBGGConfiguration
 * Creates the beans needed for the GetBoardGameFromBGG use case
 */
@Configuration
class GetBoardGameFromBGGConfiguration {

    @Bean
    public GetBoardGameFromBGGService boardGameExternalQuery(BGGApi bggApi) {
        return new GetBoardGameFromBGGService(bggApi);
    }

}
