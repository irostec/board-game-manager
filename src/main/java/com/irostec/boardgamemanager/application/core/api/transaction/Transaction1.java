package com.irostec.boardgamemanager.application.core.api.transaction;

import io.vavr.control.Either;

import java.util.function.Function;

public interface Transaction1<L, R, I> {

    Either<L, R> execute(I input);

}
