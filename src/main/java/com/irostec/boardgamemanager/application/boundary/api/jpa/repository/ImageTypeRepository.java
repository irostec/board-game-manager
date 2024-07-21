package com.irostec.boardgamemanager.application.boundary.api.jpa.repository;

import com.irostec.boardgamemanager.application.boundary.api.jpa.entity.ImageType;
import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.ImageTypeName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageTypeRepository extends JpaRepository<ImageType, Long> {

    Optional<ImageType> findByName(ImageTypeName name);

}
