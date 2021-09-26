package com.rsosor.app.security.filter;

import com.rsosor.app.cache.AbstractStringCacheStore;
import com.rsosor.app.config.properties.RsosoRProperties;
import com.rsosor.app.security.handler.ContentAuthenticationFailureHandler;
import com.rsosor.app.security.service.impl.OneTimeTokenService;
import com.rsosor.app.service.IOptionService;
import com.rsosor.app.utils.RsosoRUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ContentFilter
 *
 * @author RsosoR
 * @date 2021/9/26
 */
@Component
@Order(-1)
public class ContentFilter extends AbstractAuthenticationFilter {

    public ContentFilter(RsosoRProperties rsosorProperties,
                         IOptionService optionService,
                         AbstractStringCacheStore cacheStore,
                         OneTimeTokenService oneTimeTokenService) {
        super(rsosorProperties, optionService, cacheStore, oneTimeTokenService);

        addUrlPatterns("/**");

        String adminPattern = RsosoRUtils.ensureBoth(rsosorProperties.getAdminPath(), "/") + "**";
        addExcludeUrlPatterns(
                adminPattern,
                "/api/**",
                "/install",
                "/version",
                "/js/**",
                "/css/**");

        // set failure handler
        setFailureHandler(new ContentAuthenticationFailureHandler());
    }

    @Override
    protected String getTokenFromRequest(HttpServletRequest request) {
        return null;
    }

    @Override
    protected void doAuthenticate(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
        // Do nothing
        // create session
        request.getSession(true);
        filterChain.doFilter(request, response);
    }
}
