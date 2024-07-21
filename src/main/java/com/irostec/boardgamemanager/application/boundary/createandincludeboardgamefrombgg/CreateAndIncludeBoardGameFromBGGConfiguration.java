package com.irostec.boardgamemanager.application.boundary.createandincludeboardgamefrombgg;

import com.irostec.boardgamemanager.application.core.CreateAndIncludeBoardGameFromBGGService;
import com.irostec.boardgamemanager.application.core.api.transaction.TransactionalOperationFactory;
import com.irostec.boardgamemanager.application.core.shared.CreateBoardGameFromBGGService;
import com.irostec.boardgamemanager.application.core.shared.GetCurrentUserService;
import com.irostec.boardgamemanager.application.core.shared.IncludeBoardGameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class CreateAndIncludeBoardGameFromBGGConfiguration {

    @Bean
    CreateAndIncludeBoardGameFromBGGService createAndIncludeBoardGameFromBGGService(
        TransactionalOperationFactory transactionalOperationFactory,
        GetCurrentUserService getCurrentUserService,
        CreateBoardGameFromBGGService createBoardGameFromBGGService,
        IncludeBoardGameService includeBoardGameService
    ) {

        return new CreateAndIncludeBoardGameFromBGGService(
            transactionalOperationFactory,
            getCurrentUserService,
            createBoardGameFromBGGService,
            includeBoardGameService
        );

    }

}
