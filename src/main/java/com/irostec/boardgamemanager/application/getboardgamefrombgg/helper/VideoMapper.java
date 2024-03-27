package com.irostec.boardgamemanager.application.getboardgamefrombgg.helper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * VideoMapper
 * Maps application.shared.bggapi.output.Video instances to application.getboardgamefrombgg.output.Video instances
 */
@Mapper
public interface VideoMapper {

    VideoMapper INSTANCE = Mappers.getMapper(VideoMapper.class);

    @Mapping(source = "id", target = "externalId")
    com.irostec.boardgamemanager.application.getboardgamefrombgg.output.Video map(
            com.irostec.boardgamemanager.application.shared.bggapi.output.Video source
    );

    @Mapping(source = "externalId", target = "id")
    com.irostec.boardgamemanager.application.shared.bggapi.output.Video map(
            com.irostec.boardgamemanager.application.getboardgamefrombgg.output.Video source
    );

}
