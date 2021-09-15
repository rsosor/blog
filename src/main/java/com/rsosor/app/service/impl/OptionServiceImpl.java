package com.rsosor.app.service.impl;

import com.rsosor.app.cache.AbstractStringCacheStore;
import com.rsosor.app.event.options.OptionUpdatedEvent;
import com.rsosor.app.model.dto.OptionDTO;
import com.rsosor.app.model.dto.OptionSimpleDTO;
import com.rsosor.app.model.entity.Option;
import com.rsosor.app.model.enums.IValueEnum;
import com.rsosor.app.model.enums.PostPermalinkType;
import com.rsosor.app.model.enums.SheetPermalinkType;
import com.rsosor.app.model.params.OptionParam;
import com.rsosor.app.model.params.OptionQuery;
import com.rsosor.app.model.properties.IPropertyEnum;
import com.rsosor.app.repository.IOptionRepository;
import com.rsosor.app.service.IOptionService;
import com.rsosor.app.service.base.AbstractCrudService;
import com.rsosor.app.utils.ServiceUtils;
import com.rsosor.app.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

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

    @Override
    public void save(List<OptionParam> optionParams) {
        if (CollectionUtils.isEmpty(optionParams)) {
            return;
        }

        Map<String, Object> optionMap =
                ServiceUtils.convertToMap(optionParams, OptionParam::getKey, OptionParam::getValue);
        save(optionMap);
    }

    @Override
    public void save(OptionParam optionParam) {
        Option option = optionParam.convertTo();
        create(option);
        eventPublisher.publishEvent(new OptionUpdatedEvent(this));
    }

    @Override
    public void update(Integer optionId, OptionParam optionParam) {

    }

    @Override
    public void saveProperty(IPropertyEnum property, String value) {

    }

    @Override
    public void saveProperties(Map<? extends IPropertyEnum, String> properties) {

    }

    @Override
    public Map<String, Object> listOptions() {
        return null;
    }

    @Override
    public Map<String, Object> listOptions(Collection<String> keys) {
        return null;
    }

    @Override
    public List<OptionDTO> listDtos() {
        return null;
    }

    @Override
    public Page<OptionSimpleDTO> pageDtosBy(Pageable pageable, OptionQuery optionQuery) {
        return null;
    }

    @Override
    public Option removePermanently(Integer id) {
        return null;
    }

    @NonNull
    private Specification<Option> buildSpecByQuery(@NonNull OptionQuery optionQuery) {
        return null;
    }

    @Override
    public Object getByKeyOfNullable(String key) {
        return null;
    }

    @Override
    public Object getByKeyOfNonNull(String key) {
        return null;
    }

    @Override
    public Optional<Object> getByKey(String key) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> getByKey(String key, Class<T> valueType) {
        return Optional.empty();
    }

    @Override
    public Object getByPropertyOfNullable(IPropertyEnum property) {
        return null;
    }

    @Override
    public Object getByPropertyOfNonNull(IPropertyEnum property) {
        return null;
    }

    @Override
    public Optional<Object> getByProperty(IPropertyEnum property) {
        return Optional.empty();
    }

    @Override
    public <T> Optional<T> getByProperty(IPropertyEnum property, Class<T> propertyType) {
        return Optional.empty();
    }

    @Override
    public <T> T getByPropertyOrDefault(IPropertyEnum property, Class<T> propertyType, T defaultValue) {
        return null;
    }

    @Override
    public <T> T getByPropertyOrDefault(IPropertyEnum property, Class<T> propertyType) {
        return null;
    }

    @Override
    public <T> T getByKeyOrDefault(String key, Class<T> valueType, T defaultValue) {
        return null;
    }

    @Override
    public <T extends Enum<T>> Optional<T> getEnumByProperty(IPropertyEnum property, Class<T> valueType) {
        return Optional.empty();
    }

    @Override
    public <T extends Enum<T>> T getEnumByPropertyOrDefault(IPropertyEnum property, Class<T> valueType, T defaultValue) {
        return null;
    }

    @Override
    public <V, E extends Enum<E> & IValueEnum<V>> Optional<E> getValueEnumByProperty(IPropertyEnum property, Class<V> valueType, Class<E> enumType) {
        return Optional.empty();
    }

    @Override
    public <V, E extends Enum<E> & IValueEnum<V>> E getValueEnumByPropertyOrDefault(IPropertyEnum property, Class<V> valueType, Class<E> enumType, E defaultValue) {
        return null;
    }

    @Override
    public int getPostPageSize() {
        return 0;
    }

    @Override
    public int getArchivesPageSize() {
        return 0;
    }

    @Override
    public int getCommentPageSize() {
        return 0;
    }

    @Override
    public int getRssPageSize() {
        return 0;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public String getBlogBaseUrl() {
        return null;
    }

    @Override
    public String getBlogTitle() {
        return null;
    }

    @Override
    public String getSeoKeywords() {
        return null;
    }

    @Override
    public String getSeoDescription() {
        return null;
    }

    @Override
    public long getBirthday() {
        return 0;
    }

    @Override
    public PostPermalinkType getPostPermalinkType() {
        return null;
    }

    @Override
    public SheetPermalinkType getSheetPermalinkType() {
        return null;
    }

    @Override
    public String getSheetPrefix() {
        return null;
    }

    @Override
    public String getLinksPrefix() {
        return null;
    }

    @Override
    public String getPhotosPrefix() {
        return null;
    }

    @Override
    public String getJournalsPrefix() {
        return null;
    }

    @Override
    public String getArchivesPrefix() {
        return null;
    }

    @Override
    public String getCategoriesPrefix() {
        return null;
    }

    @Override
    public String getTagsPrefix() {
        return null;
    }

    @Override
    public String getPathSuffix() {
        return null;
    }

    @Override
    public Boolean isEnabledAbsolutePath() {
        return null;
    }

    @Override
    public OptionSimpleDTO convertToDto(Option option) {
        return null;
    }
}
