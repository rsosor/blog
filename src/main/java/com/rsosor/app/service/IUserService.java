package com.rsosor.app.service;

import com.rsosor.app.exception.NotFoundException;
import com.rsosor.app.model.entity.User;
import com.rsosor.app.service.base.ICrudService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import java.util.Optional;

/**
 * IUserService
 *
 * @author RsosoR
 * @date 2021/9/23
 */
public interface IUserService extends ICrudService<User, Integer> {

    /**
     * Gets current user.
     *
     * @return an optional user
     */
    @NonNull
    Optional<User> getCurrentUser();

    /**
     * Gets user by username.
     *
     * @param username username must not be blank
     * @return an optional user
     */
    @NonNull
    Optional<User> getByUsername(@NonNull String username);

    /**
     * Gets non null user by username.
     *
     * @param username username
     * @return user info
     * @throws NotFoundException throws when the username does not exist
     */
    @NonNull
    User getByUsernameOfNonNull(@NonNull String username);

    @NonNull
    Optional<User> getByEmail(@NonNull String email);

    @NonNull
    User getByEmailOfNonNull(@NonNull String email);

    void mustNotExpire(@NonNull User user);

    boolean passwordMatch(@NonNull User user, @Nullable String plainPassword);


}
