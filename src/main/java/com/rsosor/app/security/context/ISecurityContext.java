package com.rsosor.app.security.context;

import com.rsosor.app.security.authentication.IAuthentication;
import org.springframework.lang.Nullable;

/**
 * ISecurityContext
 *
 * @author RsosoR
 * @date 2021/9/25
 */
public interface ISecurityContext {

    /**
     * Gets the currently authenticated principal.
     *
     * @return the Authentication or null if authentication information is unavailable
     */
    @Nullable
    IAuthentication getAuthentication();

    /**
     * Changes the currently authenticated principal, or removes the authentication information.
     *
     * @param authentication the new authentication or null if no further authentication should
     *                       not be stored
     */
    void setAuthentication(@Nullable IAuthentication authentication);

    /**
     * Check if the current context has authenticated or not.
     *
     * @return true if authenticate; false otherwise
     */
    default boolean isAuthenticated() {
        return getAuthentication() != null;
    }
}
