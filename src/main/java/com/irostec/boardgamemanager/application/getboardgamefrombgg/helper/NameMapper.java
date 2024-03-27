package com.irostec.boardgamemanager.application.getboardgamefrombgg.helper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * NameMapper
 * Maps application.shared.bggapi.output.Name instances to application.getboardgamefrombgg.output.Name instances
 */
@Mapper
public interface NameMapper {

    NameMapper INSTANCE = Mappers.getMapper(NameMapper.class);

    @Mapping(target = "type",  expression = "java(source.type().name().toLowerCase())")
    com.irostec.boardgamemanager.application.getboardgamefrombgg.output.Name map(
        com.irostec.boardgamemanager.application.shared.bggapi.output.Name source
    );

    @Mapping(target = "type",  expression = "java(com.irostec.boardgamemanager.application.shared.bggapi.output.NameType.fromName(source.type()).get())")
    com.irostec.boardgamemanager.application.shared.bggapi.output.Name map(
        com.irostec.boardgamemanager.application.getboardgamefrombgg.output.Name source
    );

}
