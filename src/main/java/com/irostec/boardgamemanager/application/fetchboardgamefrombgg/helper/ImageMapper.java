package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * ImageMapper
 * Maps application.shared.bggapi.output.Image instances to application.fetchboardgamefrombgg.output.Image instances
 */
@Mapper
public interface ImageMapper {

    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    @Mapping(target = "type",  expression = "java(source.type().name().toLowerCase())")
    @Mapping(source = "link", target = "url")
    com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.Image mapImage(
            com.irostec.boardgamemanager.application.shared.bggapi.output.Image source
    );

}
