package com.irostec.boardgamemanager.configuration;

/**
 * Defines constants conforming to the Spring Expression Language (SpEL) rules.
 * @see org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
 * @see org.springframework.beans.factory.annotation.Value
 */
public final class ApplicationProperties {

    private ApplicationProperties() {}

    public static final String LOCALSTACK_ENABLED_FLAG = "aws.localStack.enabled";

}
