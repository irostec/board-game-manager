package com.irostec.boardgamemanager.application.boundary.shared.helper;

import io.micrometer.common.lang.Nullable;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.xml.bind.JAXBContext;
import retrofit2.Retrofit;

/**
 * JaxbConverterFactory
 * Used by Retrofit to convert XML responses into JAXB objects
 */
public final class JaxbConverterFactory extends Converter.Factory {

    private final Map<Type, Converter<ResponseBody, ?>> convertersByType;

    public JaxbConverterFactory(Class<?>... classes) throws JAXBException {

        final JAXBContext jaxbContext = JAXBContext.newInstance(classes);
        final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        this.convertersByType = Set.of(classes).stream()
            .collect(
                Collectors.toMap(
                    Function.identity(),
                    clazz -> new JaxbConverter<>(clazz, jaxbUnmarshaller)
                )
            );

    }

    @Override
    public @Nullable Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations,
            Retrofit retrofit
    ) {

        return this.convertersByType.get(type);

    }

    private record JaxbConverter<T>(Class<T> clazz, Unmarshaller unmarshaller)
    implements Converter<ResponseBody, T> {

        @Override
        public T convert(ResponseBody value) throws IOException {

            try {
                return clazz.cast(unmarshaller.unmarshal(new StringReader(value.string())));
            } catch (JAXBException e) {
                throw new IOException(e);
            } finally {
                value.close();
            }

        }

    }

}
