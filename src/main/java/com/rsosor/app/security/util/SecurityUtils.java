package com.rsosor.app.security.util;

import com.rsosor.app.model.entity.User;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

/**
 * SecurityUtils
 *
 * @author RsosoR
 * @date 2021/9/25
 */
public class SecurityUtils {

    /**
     * Access token cache prefix.
     */
    private static final String TOKEN_ACCESS_CACHE_PREFIX = "rsosor.admin.access.token.";

    /**
     * Refresh token cache prefix.
     */
    private static final String TOKEN_REFRESH_CACHE_PREFIX = "rsosor.admin.refresh.token.";

    private static final String ACCESS_TOKEN_CACHE_PREFIX = "rsosor.admin.access_token.";

    private static final String REFRESH_TOKEN_CACHE_PREFIX = "rsosor.admin.refresh_token.";

    private SecurityUtils() {
    }

    @NonNull
    public static String buildAccessTokenKey(@NonNull User user) {
        Assert.notNull(user, "User must not be null");

        return ACCESS_TOKEN_CACHE_PREFIX + user.getId();
    }

    @NonNull
    public static String buildRefreshTokenKey(@NonNull User user) {
        Assert.notNull(user, "User must not be null");

        return REFRESH_TOKEN_CACHE_PREFIX + user.getId();
    }

    @NonNull
    public static String buildTokenAccessKey(@NonNull String accessToken) {
        Assert.hasText(accessToken, "Refresh token must not be blank");

        return TOKEN_ACCESS_CACHE_PREFIX + accessToken;
    }

    @NonNull
    public static String buildTokenRefreshKey(@NonNull String refreshToken) {
        Assert.hasText(refreshToken, "Refresh token must not be blank");

        return TOKEN_REFRESH_CACHE_PREFIX + refreshToken;
    }

}
