package com.irostec.boardgamemanager.application.boundary.test3;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.core.api.transaction.*;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionA extends BasicTransaction0<Throwable, List<BoardGame>> {

    private final TransactionB bookCreation1;
    private final TransactionC bookCreation2;

    public TransactionA(
        TransactionalOperationFactory transactionalOperationFactory,
        TransactionB transactionB,
        TransactionC transactionC
    ) {

        super(transactionalOperationFactory);
        this.bookCreation1 = transactionB;
        this.bookCreation2 = transactionC;

    }

    protected Either<Throwable, List<BoardGame>> baseOperation() {

        return bookCreation1.execute()
                .flatMap(boardGame1 -> bookCreation2.execute().map(boardGame2 -> List.of(boardGame1, boardGame2)));

    }

}
