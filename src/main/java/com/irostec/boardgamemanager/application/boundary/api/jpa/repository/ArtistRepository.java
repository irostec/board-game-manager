package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Artist;
import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ArtistReference;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface ArtistRepository  extends JpaRepository<Artist, Long> {

    @Query(
            "SELECT new org.apache.commons.lang3.tuple.ImmutablePair(artist, artistReference) " +
            "FROM Artist artist " +
            "LEFT JOIN ArtistReference artistReference ON artist.id = artistReference.artistId " +
            "WHERE artistReference.dataSourceId = :dataSourceId AND artistReference.externalId IN :externalIds"
    )
    Stream<ImmutablePair<Artist, ArtistReference>> findByDataSourceIdAndExternalIdIn(Long dataSourceId, List<String> externalIds);

}
