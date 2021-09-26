package com.rsosor.app.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsosor.app.cache.AbstractStringCacheStore;
import com.rsosor.app.config.properties.RsosoRProperties;
import com.rsosor.app.exception.AuthenticationException;
import com.rsosor.app.exception.ForbiddenException;
import com.rsosor.app.model.properties.ApiProperties;
import com.rsosor.app.model.properties.CommentProperties;
import com.rsosor.app.security.handler.DefaultAuthenticationFailureHandler;
import com.rsosor.app.security.service.IOneTimeTokenService;
import com.rsosor.app.service.IOptionService;
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

import static com.rsosor.app.model.support.RsosoRConst.API_ACCESS_KEY_HEADER_NAME;
import static com.rsosor.app.model.support.RsosoRConst.API_ACCESS_KEY_QUERY_NAME;

/**
 * ApiAuthenticationFilter
 *
 * @author RsosoR
 * @date 2021/9/26
 */
@Slf4j
@Component
@Order(0)
public class ApiAuthenticationFilter extends AbstractAuthenticationFilter {

    private final IOptionService optionService;

    public ApiAuthenticationFilter(RsosoRProperties rsosoRProperties,
                                   IOptionService optionService,
                                   AbstractStringCacheStore cacheStore,
                                   IOneTimeTokenService oneTimeTokenService,
                                   ObjectMapper objectMapper) {
        super(rsosoRProperties, optionService, cacheStore, oneTimeTokenService);
        this.optionService = optionService;

        addUrlPatterns("/api/content/**");

        addExcludeUrlPatterns(
                "/api/content/**/comments",
                "/api/content/**/comments/**",
                "/api/content/options/comment",
                "/api/content/journals/*/likes",
                "/api/content/posts/*/likes"
        );

        // set failure handler
        DefaultAuthenticationFailureHandler failureHandler =
                new DefaultAuthenticationFailureHandler();
        failureHandler.setProductionEnv(rsosoRProperties.getMode().isProductionEnv());
        failureHandler.setObjectMapper(objectMapper);
        setFailureHandler(failureHandler);
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        if (!rsosorProperties.isAuthEnabled()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get api_enable from option
        Boolean apiEnabled =
                optionService.getByPropertyOrDefault(ApiProperties.API_ENABLED, Boolean.class, false);

        if (!apiEnabled) {
            throw new ForbiddenException("API has been disabled by blogger currently");
        }

        // Get access key
        String accessKey = getTokenFromRequest(request);

        if (StringUtils.isBlank(accessKey)) {
            // If the access key is missing
            throw new AuthenticationException("Missing API access key");
        }

        // Get access key from option
        Optional<String> optionalAccessKey =
                optionService.getByProperty(ApiProperties.API_ACCESS_KEY, String.class);

        if (optionalAccessKey.isEmpty()) {
            // If the access key is not set
            throw new AuthenticationException("API access key hasn't been set by blogger");
        }

        if (!StringUtils.equals(accessKey, optionalAccessKey.get())) {
            // If the access key is mismatch
            throw new AuthenticationException("API access key is mismatch").setErrorData(accessKey);
        }

        // Do filter
        filterChain.doFilter(request, response);
    }

    @Override
    protected String getTokenFromRequest(HttpServletRequest request) {
        return getTokenFromRequest(request, API_ACCESS_KEY_QUERY_NAME, API_ACCESS_KEY_HEADER_NAME);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        boolean result = super.shouldNotFilter(request);

        if (antPathMatcher.match("/api/content/*/comments", request.getServletPath())) {
            Boolean commentApiEnabled = optionService
                    .getByPropertyOrDefault(CommentProperties.API_ENABLED, Boolean.class, true);
            if (!commentApiEnabled) {
                // If the comment api is disabled
                result = false;
            }
        }
        return result;
    }
}
