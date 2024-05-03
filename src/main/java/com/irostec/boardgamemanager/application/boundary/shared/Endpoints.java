package com.irostec.boardgamemanager.application.boundary.shared;

import com.irostec.boardgamemanager.application.boundary.shared.bggapi.jaxb.generated.Items;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Endpoints
 * The HTTP operations exposed by boardgamegeek.com
 */
public interface Endpoints {

    @Headers({"Accept: text/xml"})
    @GET("thing?type=boardgame&videos=1&stats=1")
    Call<Items> getBoardGamesById(
        @Query("id") String id,
        @Query("type") String type,
        @Query("videos") String videos,
        @Query("stats") String stats
    );

}
