package com.irostec.boardgamemanager.application.core.api.transaction;

import io.vavr.control.Either;

import java.util.function.Supplier;

public abstract class BasicTransaction0<L, R> implements Transaction0<L, R> {

    private final Supplier<Either<L, R>> transactionalSupplier;

    public BasicTransaction0(TransactionalOperationFactory transactionalOperationFactory) {

        this.transactionalSupplier = transactionalOperationFactory.fromSupplier(this::baseOperation);

    }

    @Override
    public final Either<L, R> execute() {
        return this.transactionalSupplier.get();
    }

    protected abstract Either<L, R> baseOperation();

}
