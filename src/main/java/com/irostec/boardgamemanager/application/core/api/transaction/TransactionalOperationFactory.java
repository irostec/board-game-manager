package com.irostec.boardgamemanager.application.core.api.transaction;

import io.vavr.control.Either;

import java.util.function.Function;
import java.util.function.Supplier;

public interface TransactionalOperationFactory {

    <L, R> Supplier<Either<L, R>> fromSupplier(Supplier<Either<L, R>> supplier);

    <L, R, I> Function<I, Either<L, R>> fromFunction(Function<I, Either<L, R>> f);

}
