package com.irostec.boardgamemanager.application.core.api.transaction;

import io.vavr.control.Either;

import java.util.function.Supplier;

public interface Transaction0<L, R> {

    Either<L, R> execute();

}
