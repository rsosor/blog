package com.rsosor.app.service.impl;

import com.rsosor.app.event.options.OptionUpdatedEvent;
import com.rsosor.app.model.entity.Option;
import com.rsosor.app.model.params.OptionParam;
import com.rsosor.app.model.properties.IPropertyEnum;
import com.rsosor.app.service.IOptionService;
import com.rsosor.app.service.base.AbstractCrudService;
import com.rsosor.app.utils.ServiceUtils;
import com.rsosor.app.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * OptionServiceImpl
 *
 * @author RsosoR
 * @date 2021/9/9
 */
@Slf4j
@Service
public class OptionServiceImpl extends AbstractCrudService<Option, Integer>
        implements IOptionService {

    private final IOptionRepository optionRepository;
    private final ApplicationContext applicationContext;
    private final AbstractStringCacheStore cacheStore;
    private final Map<String, IPropertyEnum> propertyEnumMap;
    private final ApplicationEventPublisher eventPublisher;

    public OptionServiceImpl(IOptionRepository optionRepository,
                             ApplicationContext applicationContext,
                             AbstractStringCacheStore cacheStore,
                             ApplicationEventPublisher eventPublisher) {
        super(optionRepository);
        this.optionRepository = optionRepository;
        this.applicationContext = applicationContext;
        this.cacheStore = cacheStore;
        this.eventPublisher = eventPublisher;

        propertyEnumMap = Collections.unmodifiableMap(IPropertyEnum.getValuePropertyEnumMap());
    }

    @Deprecated
    @Transactional
    private void save(@NonNull String key, @Nullable String value) {
        Assert.hasText(key, "Option key must not be blank");
        save(Collections.singletonMap(key, value));
    }

    @Override
    @Transactional
    public void save(Map<String, Object> optionMap) {
        if (CollectionUtils.isEmpty(optionMap)) {
            return;
        }

        Map<String, Option> optionKeyMap = ServiceUtils.convertToMap(listAll(), Option::getKey);

        List<Option> optionsToCreate = new LinkedList<>();
        List<Option> optionsToUpdate = new LinkedList<>();

        optionMap.forEach((key, value) -> {
            Option oldOption = optionKeyMap.get(key);
            if (oldOption == null || !StringUtils.equals(oldOption.getValue(), value.toString())) {
                OptionParam optionParam = new OptionParam();
                optionParam.setKey(key);
                optionParam.setValue(value.toString());
                ValidationUtils.validate(optionParam);

                if (oldOption == null) {
                    // Create it
                    optionsToCreate.add(optionParam.convertTo());
                } else if (!StringUtils.equals(oldOption.getValue(), value.toString())) {
                    // Update it
                    optionParam.update(oldOption);
                    optionsToUpdate.add(oldOption);
                }
            }
        });

        // Update them
        updateInBatch(optionsToUpdate);

        // Create them
        createInBatch(optionsToCreate);

        if (!CollectionUtils.isEmpty(optionsToUpdate)
                || !CollectionUtils.isEmpty(optionsToCreate)) {
            // If there is something changed
            eventPublisher.publishEvent(new OptionUpdatedEvent(this));
        }
    }
}
