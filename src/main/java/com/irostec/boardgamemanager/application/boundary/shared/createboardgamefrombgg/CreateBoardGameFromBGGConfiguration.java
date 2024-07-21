package com.irostec.boardgamemanager.application.boundary.shared.createboardgamefrombgg;

import com.irostec.boardgamemanager.application.core.api.transaction.TransactionalOperationFactory;
import com.irostec.boardgamemanager.application.core.shared.BGGApi;
import com.irostec.boardgamemanager.application.core.shared.CreateBoardGameFromBGGService;
import com.irostec.boardgamemanager.application.core.shared.createboardgamefrombgg.dependency.SaveBGGBoardGameFixedData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateBoardGameFromBGGConfiguration {

    @Bean
    CreateBoardGameFromBGGService createBoardGameFromBGGService(
            TransactionalOperationFactory transactionalOperationFactory,
            BGGApi bggApi,
            SaveBGGBoardGameFixedData saveBGGBoardGameFixedData
    ) {

        return new CreateBoardGameFromBGGService(
            transactionalOperationFactory,
            bggApi,
            saveBGGBoardGameFixedData
        );

    }

}
