package com.irostec.boardgamemanager.boundary.shared;

import com.irostec.boardgamemanager.boundary.shared.helper.JaxbConverterFactory;

import jakarta.xml.bind.JAXBException;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

/**
 * BGGApiConfiguration
 * Creates the beans needed for the initialization of BoardGameGeek's API
 */
@Configuration
class BGGApiConfiguration {

    @Bean
    public JaxbConverterFactory jaxbConverterFactory() throws JAXBException {
        return new JaxbConverterFactory(com.irostec.boardgamemanager.boundary.shared.bggapi.jaxb.generated.Items.class);
    }

    @Bean
    public Endpoints bggEndpoints(@Value("${boardgamegeek.api.url}") String bggApi2Url,
                                  JaxbConverterFactory jaxbConverterFactory) throws JAXBException {

        final OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(bggApi2Url)
                .addConverterFactory(jaxbConverterFactory)
                .client(httpClient.build())
                .build();

        return retrofit.create(Endpoints.class);

    }

}
