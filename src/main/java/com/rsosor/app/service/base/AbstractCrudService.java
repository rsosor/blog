package com.rsosor.app.service.base;

import com.rsosor.app.repository.base.IBaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * AbstractCrudService
 *
 * @param <DOMAIN> domain type
 * @param <ID> id type
 * @author RsosoR
 * @date 2021/9/9
 */
@Slf4j
public abstract class AbstractCrudService<DOMAIN, ID> implements ICrudService<DOMAIN, ID> {

    private final String domainName;

    private final IBaseRepository<DOMAIN, ID> repository;

    protected AbstractCrudService(IBaseRepository<DOMAIN, ID> repository) {
        this.repository = repository;

        // Get domain name
        @SuppressWarnings("unchecked")
        Class<DOMAIN> domainClass = (Class<DOMAIN>) fetchType(0);
        domainName = domainClass.getSimpleName();
    }

    /**
     * Gets actual generic type.
     *
     * @param index generic type index
     * @return real generic type will be returned
     */
    private Type fetchType(int index) {
        Assert.isTrue(index >= 0 && index <= 1, "type index must be between 0 to 1");

        return ((ParameterizedType) this.getClass().getGenericSuperclass())
                .getActualTypeArguments()[index];
    }




}
