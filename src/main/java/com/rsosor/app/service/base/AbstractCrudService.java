package com.rsosor.app.service.base;

import lombok.extern.slf4j.Slf4j;

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

    private final BaseRepository<DOMAIN, ID> repository;

    protected AbstractCrudService(BaseRepository<DOMAIN, ID> repository) {
        this.repository = repository;

        // Get domain name
        @SuppressWarnings("unchecked")
        Class<DOMAIN> domainClass = (Class<DOMAIN>) fetchType(0);
        domainName = domainClass.getSimpleName();
    }




}
