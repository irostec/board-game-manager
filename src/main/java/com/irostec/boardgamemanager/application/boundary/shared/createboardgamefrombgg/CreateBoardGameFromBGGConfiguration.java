package com.irostec.boardgamemanager.application.boundary.shared.createboardgamefrombgg;

import com.irostec.boardgamemanager.application.core.shared.BGGApi;
import com.irostec.boardgamemanager.application.core.shared.CreateBoardGameFromBGG;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency.SaveBGGBoardGameFixedData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateBoardGameFromBGGConfiguration {

    @Bean
    CreateBoardGameFromBGG createBoardGameFromBGGService(
        BGGApi bggApi,
        SaveBGGBoardGameFixedData saveBGGBoardGameFixedData
    ) {

        return new CreateBoardGameFromBGG(bggApi, saveBGGBoardGameFixedData);

    }

}
