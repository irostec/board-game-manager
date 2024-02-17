package com.irostec.boardgamemanager.configuration.aws.dataclass;

public record ParameterStoreProperties(String parameterStorePrefix,
                                       String parameterStoreDefaultContext,
                                       String parameterStoreProfileSeparator
) { }
