package com.irostec.boardgamemanager.application.core.api.transaction;

import io.vavr.control.Either;

import java.util.function.Function;

public abstract class BasicTransaction1<L, R, I> implements Transaction1<L, R, I> {

    private final Function<I, Either<L, R>> transactionalFunction;

    public BasicTransaction1(TransactionalOperationFactory transactionalOperationFactory) {

        this.transactionalFunction = transactionalOperationFactory.fromFunction(this::baseOperation);

    }

    @Override
    public final Either<L, R> execute(I input) {
        return this.transactionalFunction.apply(input);
    }

    protected abstract Either<L, R> baseOperation(I input);

}
