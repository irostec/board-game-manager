package com.irostec.boardgamemanager.application.boundary.test3;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.BoardGame;
import com.irostec.boardgamemanager.application.boundary.api.jpa.repository.BoardGameRepository;
import com.irostec.boardgamemanager.application.core.api.transaction.BasicTransaction0;
import com.irostec.boardgamemanager.application.core.api.transaction.TransactionalOperationFactory;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

@Component
public class TransactionB extends BasicTransaction0<Throwable, BoardGame> {

    private final BoardGameRepository boardGameRepository;

    public TransactionB(TransactionalOperationFactory transactionalOperationFactory,
                        BoardGameRepository boardGameRepository) {

        super(transactionalOperationFactory);
        this.boardGameRepository = boardGameRepository;
    }

    @Override
    protected Either<Throwable, BoardGame> baseOperation() {

        BoardGame boardGame = new BoardGame();
        boardGame.setName("Board game B");

        return Try.of(
                () -> boardGameRepository.save(boardGame)
        ).toEither();

    }

}
