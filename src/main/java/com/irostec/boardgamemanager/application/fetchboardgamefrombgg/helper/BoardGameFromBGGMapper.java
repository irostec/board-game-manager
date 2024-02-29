package com.irostec.boardgamemanager.application.fetchboardgamefrombgg.helper;

import com.google.common.collect.Streams;
import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.BoardGameFromBGGWithPartitionedLinks;
import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.References;
import com.irostec.boardgamemanager.application.shared.bggapi.output.BoardGameFromBGG;
import com.irostec.boardgamemanager.application.shared.bggapi.output.Link;
import com.irostec.boardgamemanager.application.shared.bggapi.output.LinkType;
import com.irostec.boardgamemanager.application.fetchboardgamefrombgg.output.Reference;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Mapping(source = "playingTime", target = "playingTimeInMinutes")
    @Mapping(source = "minPlaytime", target = "minPlaytimeInMinutes")
    @Mapping(source = "maxPlaytime", target = "maxPlaytimeInMinutes")
    @Mapping(source = "source", target = "references", qualifiedByName = "buildReferences")
    BoardGameFromBGGWithPartitionedLinks toBoardGameFromBGGWithPartitionedLinks(BoardGameFromBGG source);

    @Mapping(source = "externalId", target = "id")
    @Mapping(source = "yearOfPublication", target = "yearPublished")
    @Mapping(source = "playingTimeInMinutes", target = "playingTime")
    @Mapping(source = "minPlaytimeInMinutes", target = "minPlaytime")
    @Mapping(source = "maxPlaytimeInMinutes", target = "maxPlaytime")
    @Mapping(source = "source", target = "links", qualifiedByName = "buildLinks")
    BoardGameFromBGG toBoardGameFromBGG(BoardGameFromBGGWithPartitionedLinks source);

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

    @Named("buildLinks")
    default Set<Link> buildLinks(BoardGameFromBGGWithPartitionedLinks source) {

        final BiFunction<Set<Reference>, LinkType, Stream<Link>> referencesToLinks = (references, linkType) ->
                references.stream().map(reference -> new Link(linkType, reference.externalId(), reference.name()));

        return Streams.concat(
                referencesToLinks.apply(source.references().categories(), LinkType.CATEGORY),
                referencesToLinks.apply(source.references().mechanics(), LinkType.MECHANIC),
                referencesToLinks.apply(source.references().families(), LinkType.FAMILY),
                referencesToLinks.apply(source.references().expansions(), LinkType.EXPANSION),
                referencesToLinks.apply(source.references().accessories(), LinkType.ACCESSORY),
                referencesToLinks.apply(source.references().integrations(), LinkType.INTEGRATION),
                referencesToLinks.apply(source.references().implementations(), LinkType.IMPLEMENTATION),
                referencesToLinks.apply(source.references().designers(), LinkType.DESIGNER),
                referencesToLinks.apply(source.references().artists(), LinkType.ARTIST),
                referencesToLinks.apply(source.references().publishers(), LinkType.PUBLISHER)
        ).collect(Collectors.toSet());

    }


}
