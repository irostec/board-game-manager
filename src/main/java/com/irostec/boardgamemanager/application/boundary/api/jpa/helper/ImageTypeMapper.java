package com.irostec.boardgamemanager.application.boundary.api.jpa.helper;

import com.irostec.boardgamemanager.application.boundary.api.jpa.enumeration.ImageTypeName;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.irostec.boardgamemanager.application.core.shared.bggapi.output.ImageType;

@Mapper
public interface ImageTypeMapper {

    ImageTypeMapper INSTANCE = Mappers.getMapper(ImageTypeMapper.class);

    default ImageTypeName map(ImageType imageType) {

        return switch (imageType) {
            case THUMBNAIL -> ImageTypeName.THUMBNAIL;
            case IMAGE -> ImageTypeName.IMAGE;
        };

    }

}
