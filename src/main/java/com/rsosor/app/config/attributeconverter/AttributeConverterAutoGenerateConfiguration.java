package com.rsosor.app.config.attributeconverter;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilderCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * Jpa configuration.
 *
 * @author RsosoR
 * @date 2021/9/16
 */
public class AttributeConverterAutoGenerateConfiguration {

    @Bean
    EntityManagerFactoryBuilderCustomizer entityManagerFactoryBuilderCustomizer(
            ConfigurableListableBeanFactory factory) {
        return builder -> builder.setPersistenceUnitPostProcessors(
                new AutoGenerateConverterPersistenceUnitPostProcessor(factory));
    }
}
