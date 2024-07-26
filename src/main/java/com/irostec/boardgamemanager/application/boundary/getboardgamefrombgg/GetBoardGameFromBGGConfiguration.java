package com.irostec.boardgamemanager.application.boundary.getboardgamefrombgg;

import com.irostec.boardgamemanager.application.core.GetBoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.BGGApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GetBoardGameFromBGGConfiguration
 * Creates the beans needed for the GetBoardGameFromBGG use case
 */
@Configuration
class GetBoardGameFromBGGConfiguration {

    @Bean
    public GetBoardGameFromBGG boardGameExternalQuery(BGGApi bggApi) {
        return new GetBoardGameFromBGG(bggApi);
    }

}
