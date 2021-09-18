package com.rsosor.app.service.impl;

import com.rsosor.app.cache.AbstractStringCacheStore;
import com.rsosor.app.event.options.OptionUpdatedEvent;
import com.rsosor.app.exception.MissingPropertyException;
import com.rsosor.app.model.dto.OptionDTO;
import com.rsosor.app.model.dto.OptionSimpleDTO;
import com.rsosor.app.model.entity.Option;
import com.rsosor.app.model.enums.IValueEnum;
import com.rsosor.app.model.enums.PostPermalinkType;
import com.rsosor.app.model.enums.SheetPermalinkType;
import com.rsosor.app.model.params.OptionParam;
import com.rsosor.app.model.params.OptionQuery;
import com.rsosor.app.model.properties.*;
import com.rsosor.app.repository.IOptionRepository;
import com.rsosor.app.service.IOptionService;
import com.rsosor.app.service.base.AbstractCrudService;
import com.rsosor.app.utils.DateUtils;
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

import javax.persistence.criteria.Predicate;
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
        Option optionToUpdate = getById(optionId);
        optionParam.update(optionToUpdate);
        update(optionToUpdate);
        eventPublisher.publishEvent(new OptionUpdatedEvent(this));
    }

    @Override
    public void saveProperty(IPropertyEnum property, String value) {
        Assert.notNull(property, "Property must not be null");

        save(property.getValue(), value);
    }

    @Override
    public void saveProperties(Map<? extends IPropertyEnum, String> properties) {
        if (CollectionUtils.isEmpty(properties)) {
            return;
        }

        Map<String, Object> optionMap = new LinkedHashMap<>();

        properties.forEach((property, value) -> optionMap.put(property.getValue(), value));

        save(optionMap);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> listOptions() {
        // Get options from cache
        return cacheStore.getAny(OPTIONS_KEY, Map.class).orElseGet(() -> {
            List<Option> options = listAll();

            Set<String> keys = ServiceUtils.fetchProperty(options, Option::getKey);

            Map<String, Object> userDefinedOptionMap =
                    ServiceUtils.convertToMap(options, Option::getKey, option -> {
                        String key = option.getKey();

                        IPropertyEnum propertyEnum = propertyEnumMap.get(key);

                        if (propertyEnum == null) {
                            return option.getValue();
                        }

                        return IPropertyEnum.convertTo(option.getValue(), propertyEnum);
                    });

            Map<String, Object> result = new HashMap<>(userDefinedOptionMap);

            // Add default property
            propertyEnumMap.keySet()
                    .stream()
                    .filter(key -> !keys.contains(key))
                    .forEach(key -> {
                        IPropertyEnum propertyEnum = propertyEnumMap.get(key);

                        if (StringUtils.isBlank(propertyEnum.defaultValue())) {
                            return;
                        }

                        result.put(key,
                                IPropertyEnum.convertTo(propertyEnum.defaultValue(), propertyEnum));
                    });
            // Cache the result
            cacheStore.putAny(OPTIONS_KEY, result);

            return result;
        });
    }

    @Override
    public Map<String, Object> listOptions(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptyMap();
        }

        Map<String, Object> optionMap = listOptions();

        Map<String, Object> result = new HashMap<>(keys.size());

        keys.stream()
                .filter(optionMap::containsKey)
                .forEach(key -> result.put(key, optionMap.get(key)));

        return result;
    }

    @Override
    public List<OptionDTO> listDtos() {
        List<OptionDTO> result = new LinkedList<>();

        listOptions().forEach((key, value) -> result.add(new OptionDTO(key, value)));

        return result;
    }

    @Override
    public Page<OptionSimpleDTO> pageDtosBy(Pageable pageable, OptionQuery optionQuery) {
        Assert.notNull(pageable, "Page info must not be null");

        Page<Option> optionPage = optionRepository.findAll(buildSpecByQuery(optionQuery), pageable);

        return optionPage.map(this::convertToDto);
    }

    @Override
    public Option removePermanently(Integer id) {
        Option deletedOption = removeById(id);
        eventPublisher.publishEvent(new OptionUpdatedEvent(this));
        return deletedOption;
    }

    @NonNull
    private Specification<Option> buildSpecByQuery(@NonNull OptionQuery optionQuery) {
        Assert.notNull(optionQuery, "Option query must not be null");

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new LinkedList<>();

            if (optionQuery.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), optionQuery.getType()));
            }

            if (optionQuery.getKeyword() != null) {

                String likeCondition =
                        String.format("%%%s%%", StringUtils.strip(optionQuery.getKeyword()));

                Predicate keyLike = criteriaBuilder.like(root.get("key"), likeCondition);

                Predicate valueLike = criteriaBuilder.like(root.get("key"), likeCondition);

                predicates.add(criteriaBuilder.or(keyLike, valueLike));
            }

            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public Object getByKeyOfNullable(String key) {
        return getByKey(key).orElse(null);
    }

    @Override
    public Object getByKeyOfNonNull(String key) {
        return getByKey(key).orElseThrow(
                () -> new MissingPropertyException("You have to config " + key + " setting"));
    }

    @Override
    public Optional<Object> getByKey(String key) {
        Assert.hasText(key, "Option key must not be blank");

        return Optional.ofNullable(listOptions().get(key));
    }

    @Override
    public <T> Optional<T> getByKey(String key, Class<T> valueType) {
        return getByKey(key).map(value -> IPropertyEnum.convertTo(value.toString(), valueType));
    }

    @Override
    public Object getByPropertyOfNullable(IPropertyEnum property) {
        return getByProperty(property).orElse(null);
    }

    @Override
    public Object getByPropertyOfNonNull(IPropertyEnum property) {
        Assert.notNull(property, "Blog property must not be null");

        return getByKeyOfNonNull(property.getValue());
    }

    @Override
    public Optional<Object> getByProperty(IPropertyEnum property) {
        Assert.notNull(property, "Blog property must not be null");

        return getByKey(property.getValue());
    }

    @Override
    public <T> Optional<T> getByProperty(IPropertyEnum property, Class<T> propertyType) {
        return getByProperty(property)
                .map(propertyValue -> IPropertyEnum.convertTo(propertyValue.toString(), propertyType));
    }

    @Override
    public <T> T getByPropertyOrDefault(IPropertyEnum property, Class<T> propertyType, T defaultValue) {
        Assert.notNull(property, "Blog property must not be null");

        return getByProperty(property, propertyType).orElse(defaultValue);
    }

    @Override
    public <T> T getByPropertyOrDefault(IPropertyEnum property, Class<T> propertyType) {
        return getByProperty(property, propertyType).orElse(property.defaultValue(propertyType));
    }

    @Override
    public <T> T getByKeyOrDefault(String key, Class<T> valueType, T defaultValue) {
        return getByKey(key, valueType).orElse(defaultValue);
    }

    @Override
    public <T extends Enum<T>> Optional<T> getEnumByProperty(IPropertyEnum property, Class<T> valueType) {
        return getByProperty(property)
                .map(value -> IPropertyEnum.convertToEnum(value.toString(), valueType));
    }

    @Override
    public <T extends Enum<T>> T getEnumByPropertyOrDefault(IPropertyEnum property, Class<T> valueType, T defaultValue) {
        return getEnumByProperty(property, valueType).orElse(defaultValue);
    }

    @Override
    public <V, E extends Enum<E> & IValueEnum<V>> Optional<E> getValueEnumByProperty(IPropertyEnum property, Class<V> valueType, Class<E> enumType) {
        return getByProperty(property).map(value -> IValueEnum
                .valueToEnum(enumType, IPropertyEnum.convertTo(value.toString(), valueType)));
    }

    @Override
    public <V, E extends Enum<E> & IValueEnum<V>> E getValueEnumByPropertyOrDefault(IPropertyEnum property,
                Class<V> valueType, Class<E> enumType, E defaultValue) {
        return getValueEnumByProperty(property, valueType, enumType).orElse(defaultValue);
    }

    @Override
    public int getPostPageSize() {
        try {
            return getByPropertyOrDefault(PostProperties.INDEX_PAGE_SIZE, Integer.class,
                    DEFAULT_POST_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(PostProperties.INDEX_PAGE_SIZE.getValue() + " option is not a number format", e);
            return DEFAULT_POST_PAGE_SIZE;
        }
    }

    @Override
    public int getArchivesPageSize() {
        try {
            return getByPropertyOrDefault(PostProperties.ARCHIVES_PAGE_SIZE, Integer.class,
                    DEFAULT_ARCHIVES_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(
                    PostProperties.ARCHIVES_PAGE_SIZE.getValue() + " option is not a number format", e);
            return DEFAULT_POST_PAGE_SIZE;
        }
    }

    @Override
    public int getCommentPageSize() {
        try {
            return getByPropertyOrDefault(CommentProperties.PAGE_SIZE, Integer.class,
                    DEFAULT_COMMENT_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(CommentProperties.PAGE_SIZE.getValue() + " option is not a number format", e);
            return DEFAULT_COMMENT_PAGE_SIZE;
        }
    }

    @Override
    public int getRssPageSize() {
        try {
            return getByPropertyOrDefault(PostProperties.RSS_PAGE_SIZE, Integer.class,
                    DEFAULT_RSS_PAGE_SIZE);
        } catch (NumberFormatException e) {
            log.error(PostProperties.RSS_PAGE_SIZE.getValue() + " setting is not a nubmer format", e);
            return DEFAULT_RSS_PAGE_SIZE;
        }
    }

    @Override
    public Locale getLocale() {
        return getByProperty(BlogProperties.BLOG_LOCALE).map(localeStr -> {
            try {
                return Locale.forLanguageTag(localeStr.toString());
            } catch (Exception e) {
                return Locale.getDefault();
            }
        }).orElseGet(Locale::getDefault);
    }

    @Override
    public String getBlogBaseUrl() {
        // Get server port
        String serverPort = applicationContext.getEnvironment().getProperty("server.port", "8080");

        String blogUrl = getByProperty(BlogProperties.BLOG_URL).orElse("").toString();

        if (StringUtils.isNotBlank(blogUrl)) {
            blogUrl = StringUtils.removeEnd(blogUrl, "/");
        } else {
            blogUrl = String.format("http://$s:$s", "127.0.0.1", serverPort);
        }

        return blogUrl;
    }

    @Override
    public String getBlogTitle() {
        return getByProperty(BlogProperties.BLOG_TITLE).orElse("").toString();
    }

    @Override
    public String getSeoKeywords() {
        return getByProperty(SeoProperties.KEYWORDS).orElse("").toString();
    }

    @Override
    public String getSeoDescription() {
        return getByProperty(SeoProperties.DESCRIPTION).orElse("").toString();
    }

    @Override
    public long getBirthday() {
        return getByProperty(PrimaryProperties.BIRTHDAY, Long.class).orElseGet(() -> {
            long currentTime = DateUtils.now().getTime();
            saveProperty(PrimaryProperties.BIRTHDAY, String.valueOf(currentTime));
            return currentTime;
        });
    }

    @Override
    public PostPermalinkType getPostPermalinkType() {
        return getEnumByPropertyOrDefault(PermalinkProperties.POST_PERMALINK_TYPE,
                PostPermalinkType.class, PostPermalinkType.DEFAULT);
    }

    @Override
    public SheetPermalinkType getSheetPermalinkType() {
        return getEnumByPropertyOrDefault(PermalinkProperties.SHEET_PERMALINK_TYPE,
                SheetPermalinkType.class, SheetPermalinkType.SECONDARY);
    }

    @Override
    public String getSheetPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.SHEET_PREFIX, String.class,
                PermalinkProperties.SHEET_PREFIX.defaultValue());
    }

    @Override
    public String getLinksPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.LINKS_PREFIX, String.class,
                PermalinkProperties.LINKS_PREFIX.defaultValue());
    }

    @Override
    public String getPhotosPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.PHOTOS_PREFIX, String.class,
                PermalinkProperties.PHOTOS_PREFIX.defaultValue());
    }

    @Override
    public String getJournalsPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.JOURNALS_PREFIX, String.class,
                PermalinkProperties.JOURNALS_PREFIX.defaultValue());
    }

    @Override
    public String getArchivesPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.ARCHIVES_PREFIX, String.class,
                PermalinkProperties.ARCHIVES_PREFIX.defaultValue());
    }

    @Override
    public String getCategoriesPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.CATEGORIES_PREFIX, String.class,
                PermalinkProperties.CATEGORIES_PREFIX.defaultValue());
    }

    @Override
    public String getTagsPrefix() {
        return getByPropertyOrDefault(PermalinkProperties.TAGS_PREFIX, String.class,
                PermalinkProperties.TAGS_PREFIX.defaultValue());
    }

    @Override
    public String getPathSuffix() {
        return getByPropertyOrDefault(PermalinkProperties.PATH_SUFFIX, String.class,
                PermalinkProperties.PATH_SUFFIX.defaultValue());
    }

    @Override
    public Boolean isEnabledAbsolutePath() {
        return getByPropertyOrDefault(OtherProperties.GLOBAL_ABSOLUTE_PATH_ENABLED, Boolean.class,
                true);
    }

    @Override
    public OptionSimpleDTO convertToDto(Option option) {
        Assert.notNull(option, "Option must not be null");

        return new OptionSimpleDTO().convertFrom(option);
    }
}
