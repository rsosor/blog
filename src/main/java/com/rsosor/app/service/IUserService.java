package com.rsosor.app.service;


import com.rsosor.app.model.entity.User;
import com.rsosor.app.service.base.ICrudService;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * IUserService
 *
 * @author RsosoR
 * @date 2021/9/23
 */
public interface IUserService extends ICrudService<User, Integer> {

    @NonNull
    User getByUsernameOfNonNull(@NonNull String username);

    @NonNull
    User getByEmailOfNonNull(@NonNull String email);

    void mustNotExpire(@NonNull User user);

    boolean passwordMatch(@NonNull User user, @Nullable String plainPassword);


}
