package com.rsosor.app.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsosor.app.cache.AbstractStringCacheStore;
import com.rsosor.app.config.properties.RsosoRProperties;
import com.rsosor.app.exception.AuthenticationException;
import com.rsosor.app.model.entity.User;
import com.rsosor.app.security.authentication.AuthenticationImpl;
import com.rsosor.app.security.context.SecurityContextHolder;
import com.rsosor.app.security.context.SecurityContextImpl;
import com.rsosor.app.security.handler.DefaultAuthenticationFailureHandler;
import com.rsosor.app.security.service.impl.IOneTimeTokenService;
import com.rsosor.app.security.support.UserDetail;
import com.rsosor.app.security.util.SecurityUtils;
import com.rsosor.app.service.IOptionService;
import com.rsosor.app.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.rsosor.app.model.support.RsosoRConst.ADMIN_TOKEN_HEADER_NAME;
import static com.rsosor.app.model.support.RsosoRConst.ADMIN_TOKEN_QUERY_NAME;

/**
 * AdminAuthenticationFilter
 *
 * @author RsosoR
 * @date 2021/9/26
 */
@Slf4j
@Component
@Order(1)
public class AdminAuthenticationFilter extends AbstractAuthenticationFilter {

    private final RsosoRProperties rsosorProperties;

    private final IUserService userService;

    public AdminAuthenticationFilter(AbstractStringCacheStore cacheStore,
                                     IUserService userService,
                                     RsosoRProperties rsosorProperties,
                                     IOptionService optionService,
                                     IOneTimeTokenService oneTimeTokenService,
                                     ObjectMapper objectMapper) {
        super(rsosorProperties, optionService, cacheStore, oneTimeTokenService);
        this.userService = userService;
        this.rsosorProperties = rsosorProperties;

        addUrlPatterns("/api/admin/**", "/api/content/comments");

        addExcludeUrlPatterns(
                "/api/admin/login",
                "/api/admin/refresh/*",
                "/api/admin/installations",
                "/api/admin/migrations/rsosor",
                "/api/admin/is_installed",
                "/api/admin/password/code",
                "/api/admin/password/reset",
                "/api/admin/login/precheck"
        );

        // set failure handler
        DefaultAuthenticationFailureHandler failureHandler =
                new DefaultAuthenticationFailureHandler();
        failureHandler.setProductionEnv(rsosorProperties.getMode().isProductionEnv());
        failureHandler.setObjectMapper(objectMapper);

        setFailureHandler(failureHandler);
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

        if (!rsosorProperties.isAuthEnabled()) {
            // Set security
            userService.getCurrentUser().ifPresent(user ->
                    SecurityContextHolder.setContext(
                            new SecurityContextImpl(new AuthenticationImpl(new UserDetail(user)))));

            // Do filter
            filterChain.doFilter(request, response);
            return;
        }

        // Get token from request
        String token = getTokenFromRequest(request);

        if (StringUtils.isBlank(token)) {
            throw new AuthenticationException("未登入，請登入後再試一次");
        }

        // Get user id from cache
        Optional<Integer> optionalUserId =
                cacheStore.getAny(SecurityUtils.buildTokenAccessKey(token), Integer.class);

        if (!optionalUserId.isPresent()) {
            throw new AuthenticationException("Token 已過期或不存在").setErrorData(token);
        }

        // Get the user
        User user = userService.getById(optionalUserId.get());

        // Build user detail
        UserDetail userDetail = new UserDetail(user);

        // Set security
        SecurityContextHolder
                .setContext(new SecurityContextImpl(new AuthenticationImpl(userDetail)));

        // Do filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected String getTokenFromRequest(HttpServletRequest request) {
        return getTokenFromRequest(request, ADMIN_TOKEN_QUERY_NAME, ADMIN_TOKEN_HEADER_NAME);
    }
}
