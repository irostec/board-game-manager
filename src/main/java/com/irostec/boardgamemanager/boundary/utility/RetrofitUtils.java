package com.irostec.boardgamemanager.boundary.utility;

import com.irostec.boardgamemanager.common.exception.BGMException;
import com.irostec.boardgamemanager.common.exception.ExternalServerCallException;
import com.irostec.boardgamemanager.common.exception.ExternalServerHttpException;
import io.atlassian.fugue.Checked;
import io.atlassian.fugue.Eithers;
import retrofit2.Call;
import retrofit2.Response;

/**
 * RetrofitUtils
 * Convenient methods to handle Retrofit's Call instances
 */
public final class RetrofitUtils {

    private RetrofitUtils() {}

    public static <T> T handleCall(Call<T> call) throws BGMException {

        return Eithers.getOrThrow(
                Checked.of(() -> call.execute())
                    .toEither()
                    .leftMap(RetrofitUtils::wrapInExternalServerCallException)
                    .flatMap(
                        response -> Eithers.cond(response.isSuccessful(),
                                    new ExternalServerHttpException(unsuccessfulResponseToString(response)),
                                    response.body())
                    )
        );

    }

    // This would be unnecessary if Java's type inference was more adequate, but...
    private static BGMException wrapInExternalServerCallException(Exception exception) {
        return new ExternalServerCallException(exception);
    }

    private static String unsuccessfulResponseToString(Response<?> response) {
        return String.format("HTTP code: %d, Error Message: %s", response.code(), response.message());
    }

}
