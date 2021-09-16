package com.rsosor.app.config.attributeconverter;

import com.rsosor.app.model.enums.IValueEnum;
import com.rsosor.app.model.properties.IPropertyEnum;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;
import org.springframework.util.ClassUtils;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

/**
 * AutoGenerateConverterPersistenceUnitPostProcessor
 *
 * @author RsosoR
 * @date 2021/9/16
 */
class AutoGenerateConverterPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

    private static final String PACKAGE_TO_SCAN = "com.rsosor.app";

    private final ConfigurableListableBeanFactory factory;

    public AutoGenerateConverterPersistenceUnitPostProcessor(
            ConfigurableListableBeanFactory factory) {
        this.factory = factory;
    }

    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        var generator = new AttributeConverterAutoGenerator(factory.getBeanClassLoader());

        findIValueEnumClasses()
                .stream()
                .map(generator::generate)
                .map(Class::getName)
                .forEach(pui::addManagedClassName);
    }

    private Set<Class<?>> findIValueEnumClasses() {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        // include IValueEnum class
        scanner.addIncludeFilter(new AssignableTypeFilter(IValueEnum.class));
        // exclude IPropertyEnum class
        scanner.addExcludeFilter(new AssignableTypeFilter(IPropertyEnum.class));

        return scanner.findCandidateComponents(PACKAGE_TO_SCAN)
                .stream()
                .filter(bd -> bd.getBeanClassName() != null)
                .map(bd -> ClassUtils.resolveClassName(bd.getBeanClassName(), null))
                .collect(toUnmodifiableSet());
    }
}
