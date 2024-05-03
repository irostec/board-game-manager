package com.irostec.boardgamemanager.application.core.getboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * ImageMapper
 * Maps application.shared.bggapi.output.Image instances to application.getboardgamefrombgg.output.Image instances
 */
@Mapper
public interface ImageMapper {

    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);

    @Mapping(target = "type",  expression = "java(source.type().name().toLowerCase())")
    @Mapping(source = "link", target = "url")
    Image map(
            com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image source
    );

    @Mapping(target = "type",  expression = "java(com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType.valueOf(source.type().toUpperCase()))")
    @Mapping(source = "url", target = "link")
    com.irostec.boardgamemanager.application.core.shared.bggapi.output.Image map(
            Image source
    );

}
