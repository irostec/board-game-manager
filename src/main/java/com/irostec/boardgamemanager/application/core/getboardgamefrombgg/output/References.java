package com.irostec.boardgamemanager.application.core.getboardgamefrombgg.output;

import java.util.Set;

/**
 * References
 * Groups references from boardgamegeek.com according to their type
 */
public record References(
        Set<Reference> categories,
        Set<Reference> mechanics,
        Set<Reference> families,
        Set<Reference> expansions,
        Set<Reference> accessories,
        Set<Reference> integrations,
        Set<Reference> implementations,
        Set<Reference> designers,
        Set<Reference> artists,
        Set<Reference> publishers
) {}
