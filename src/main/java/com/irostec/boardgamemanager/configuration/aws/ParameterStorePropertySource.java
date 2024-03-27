package com.irostec.boardgamemanager.configuration.aws;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.Parameter;

import com.irostec.boardgamemanager.configuration.aws.dataclass.ParameterStoreProperties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.Environment;

import static org.apache.commons.lang3.StringUtils.startsWithAny;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static com.irostec.boardgamemanager.common.utility.LoggingUtils.info;
import static com.irostec.boardgamemanager.common.utility.LoggingUtils.warn;
import static com.google.common.base.Defaults.defaultValue;

import io.vavr.control.Try;

import java.util.Arrays;
import java.util.Optional;

/**
 * ParameterStorePropertySource
 * A custom property source backed by AWS Systems Manager Parameter Store.
 * Applies the same property resolution mechanism used by Spring Cloud AWS.
 * Ideally, this implementation would be unnecessary, since we would just have to change an environment variable to specify the URL of the AWS instance.
 * AWS does provide a mechanism to specify a custom endpoint, but it isn't available in all SDKs, and unfortunately, the AWS SDK for Java 2.x is one of the unsupported ones.
 * @see <a href="https://cloud.spring.io/spring-cloud-static/spring-cloud-aws/2.0.0.RELEASE/multi/multi__cloud_environment.html#_integrating_your_spring_cloud_application_with_the_aws_parameter_store">the Spring Cloud AWS documentation</a> for more information about the property resolution mechanism.
 * @see <a href="https://docs.aws.amazon.com/sdkref/latest/guide/feature-ss-endpoints.html">the AWS SDKs and Tools Reference Guide</a> for more information about the AWS SDK support for custom endpoints.
 */
class ParameterStorePropertySource extends PropertySource<AWSSimpleSystemsManagement> {

    private static final String[] VALID_PARAMETER_PREFIXES = {"boardgamegeek.", "aws.dynamodb.", "spring.datasource."};
    private static final String SEPARATOR = "/";

    private final Logger logger = LogManager.getLogger(ParameterStorePropertySource.class);

    private final Environment environment;

    private final ParameterStoreProperties parameterStoreProperties;

    public ParameterStorePropertySource(Environment environment,
                                        String name,
                                        AWSSimpleSystemsManagement source,
                                        ParameterStoreProperties parameterStoreProperties) {
        super(name, source);
        this.environment = environment;
        this.parameterStoreProperties = parameterStoreProperties;
    }

    @Override
    public String getProperty(String parameterName) {

        return !startsWithAny(parameterName, VALID_PARAMETER_PREFIXES) ?
                defaultValue(String.class) :
                Arrays.stream(this.environment.getActiveProfiles())
                .map(activeProfile -> getParameterValue(Optional.of(activeProfile), parameterName))
                .findFirst()
                .orElseGet(() -> getParameterValue(Optional.empty(), parameterName));

    }

    private String getParameterValue(Optional<String> profileContainer, String parameterName) {

        final String profileSuffix =
                profileContainer.map(profile -> parameterStoreProperties.parameterStoreProfileSeparator() + profile)
                .orElse(EMPTY);

        final String expandedContext = parameterStoreProperties.parameterStoreDefaultContext() + profileSuffix;

        final String expandedParameterName = String.join(SEPARATOR,
                parameterStoreProperties.parameterStorePrefix(),
                expandedContext,
                parameterName);

        final String methodName = "getParameterValue";

        GetParameterRequest parameterRequest = new GetParameterRequest()
                .withName(expandedParameterName)
                .withWithDecryption(true);

        info(logger, methodName, String.format("Attempting to retrieve parameter with request %s", parameterRequest));

        Try<Parameter> tentativeParameter = Try.of(() -> source.getParameter(parameterRequest).getParameter());

        return tentativeParameter.fold(
                exception -> {
                    warn(logger, methodName, exception);
                    return defaultValue(String.class);
                },
                Parameter::getValue
        );

    }

}
