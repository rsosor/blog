package com.rsosor.app.repository;

import com.rsosor.app.model.entity.User;
import com.rsosor.app.repository.base.IBaseRepository;
import org.springframework.lang.NonNull;
import java.util.Optional;

/**
 * IUserRepository
 *
 * @author RsosoR
 * @date 2021/9/16
 */
public interface IUserRepository extends IBaseRepository<User, Integer> {

    /**
     * Gets user by username.
     *
     * @param username username msut not be blank
     * @return an optional user
     */
    @NonNull
    Optional<User> findByUsername(@NonNull String username);

    /**
     * Gets user by email.
     *
     * @param email email must not be blank
     * @return an optional user
     */
    @NonNull
    Optional<User> findByEmail(@NonNull String email);
}
