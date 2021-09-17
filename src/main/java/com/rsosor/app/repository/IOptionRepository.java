package com.rsosor.app.repository;

import com.rsosor.app.model.entity.Option;
import com.rsosor.app.repository.base.IBaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * IOptionRepository
 *
 * @author RsosoR
 * @date 2021/9/12
 */
public interface IOptionRepository extends IBaseRepository<Option, Integer>, JpaSpecificationExecutor<Option> {

    /**
     * Query option by key
     *
     * @param key key
     * @return option
     */
    Optional<Option> findByKey(String key);

    /**
     * Delete option by key
     *
     * @param key key
     */
    void deleteByKey(String key);
}
