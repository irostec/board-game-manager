package com.irostec.boardgamemanager.boundary.utility;

import com.irostec.boardgamemanager.common.error.HttpError;
import com.irostec.boardgamemanager.common.error.NetworkFailure;
import com.irostec.boardgamemanager.common.error.UnsuccessfulResponse;
import io.vavr.control.Either;
import io.vavr.control.Try;
import retrofit2.Call;
import retrofit2.Response;

/**
 * RetrofitUtils
 * Convenient methods to handle Retrofit's Call instances
 */
public final class RetrofitUtils {

    private RetrofitUtils() {}

    public static <T> Either<HttpError, T> handleCall(Call<T> call) {

        Either<HttpError, Response<T>> responseContainer = Try.of(call::execute)
                .toEither()
                .mapLeft(NetworkFailure::new);

        return responseContainer.flatMap(response ->
                        response.isSuccessful() ?
                                Either.right(response.body()) :
                                Either.left(
                                        new UnsuccessfulResponse(response.code(), response.message())
                                )
                );

    }

}
