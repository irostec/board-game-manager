package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.References;
import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.application.shared.bggapi.output.LinkType;
import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.Reference;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * BoardGameFromBGGMapper
 * Maps BoardGameFromBGG instances to BoardGameFromBGGWithPartitionedLinks instances
 */
@Mapper(uses = {NameMapper.class, ImageMapper.class, VideoMapper.class})
public interface BoardGameFromBGGMapper {

    BoardGameFromBGGMapper INSTANCE = Mappers.getMapper(BoardGameFromBGGMapper.class);

    @Mapping(source = "id", target = "externalId")
    @Mapping(source = "yearPublished", target = "yearOfPublication")
    @Mapping(source = "minPlaytime", target = "minPlaytimeInMinutes")
    @Mapping(source = "maxPlaytime", target = "maxPlaytimeInMinutes")
    @Mapping(source = "source", target = "references", qualifiedByName = "buildReferences")
    BoardGameFromBGGWithPartitionedLinks toBoardGameFromBGGWithPartitionedLinks(BoardGameFromBGG source);

    @Named("buildReferences")
    default References buildReferences(BoardGameFromBGG source) {

        Map<LinkType, List<Reference>> listsOfReferencesByLinkType = new HashMap<>();

        Arrays.stream(LinkType.values())
                .forEach(linkType -> listsOfReferencesByLinkType.put(linkType, new ArrayList<>()));

        source.links().forEach(link ->
             listsOfReferencesByLinkType.get(link.type()).add(new Reference(link.id(), link.value()))
        );

        return new References(
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.CATEGORY)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.MECHANIC)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.FAMILY)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.EXPANSION)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.ACCESSORY)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.INTEGRATION)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.IMPLEMENTATION)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.DESIGNER)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.ARTIST)),
            Set.copyOf(listsOfReferencesByLinkType.get(LinkType.PUBLISHER))
        );

    }

}
