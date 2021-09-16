package com.rsosor.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsosor.app.cache.AbstractStringCacheStore;
import com.rsosor.app.cache.LevelCacheStore;
import com.rsosor.app.config.attributeconverter.AttributeConverterAutoGenerateConfiguration;
import com.rsosor.app.config.properties.RsosoRProperties;
import com.rsosor.app.repository.base.BaseRepositoryImpl;
import com.rsosor.app.utils.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * RsosoRConfiguration
 *
 * @author RsosoR
 * @date 2021/9/16
 */
@Slf4j
@EnableAsync
@EnableScheduling
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(RsosoRProperties.class)
@EnableJpaRepositories(basePackages = "com.rsosor.app.repository", repositoryBaseClass = BaseRepositoryImpl.class)
@Import(AttributeConverterAutoGenerateConfiguration.class)
public class RsosoRConfiguration {

    private final RsosoRProperties rsosorProperties;

    public RsosoRConfiguration(RsosoRProperties rsosorProperties) {
        this.rsosorProperties = rsosorProperties;
    }

    @Bean
    ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        builder.failOnEmptyBeans(false);
        return builder.build();
    }

    @Bean
    RestTemplate httpsRestTemplate(RestTemplateBuilder builder)
            throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        RestTemplate httpsRestTemplate = builder.build();
        httpsRestTemplate.setRequestFactory(
                new HttpComponentsClientHttpRequestFactory(HttpClientUtils.createHttpsClient(
                        (int) RsosoRProperties.getDownloadTimeout().toMillis())));
        return httpsRestTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    AbstractStringCacheStore stringCacheStore() {
        AbstractStringCacheStore stringCacheStore;
        switch (RsosoRProperties.getCache()) {
            case "level":
                stringCacheStore = new LevelCacheStore(this.rsosorProperties);
                break;
            case "memory":
            default:
                // memory or default
                stringCacheStore = new InMemoryCacheStore();
                break;
        }
    }

}
