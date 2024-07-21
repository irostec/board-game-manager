package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.stream.Stream;

public interface ImageRepository extends JpaRepository<Image, Long> {

    Stream<Image> findByBoardGameReferenceIdAndUrlIn(Long boardGameReferenceId, Collection<String> urls);

}
