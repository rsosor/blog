package com.rsosor.app.security.filter;

import com.rsosor.app.cache.AbstractStringCacheStore;
import com.rsosor.app.config.properties.RsosoRProperties;
import com.rsosor.app.exception.AbstractRsosoRException;
import com.rsosor.app.exception.BadRequestException;
import com.rsosor.app.exception.ForbiddenException;
import com.rsosor.app.exception.NotInstallException;
import com.rsosor.app.model.enums.Mode;
import com.rsosor.app.model.properties.PrimaryProperties;
import com.rsosor.app.security.context.SecurityContextHolder;
import com.rsosor.app.security.handler.IAuthenticationFailureHandler;
import com.rsosor.app.security.handler.DefaultAuthenticationFailureHandler;
import com.rsosor.app.security.service.IOneTimeTokenService;
import com.rsosor.app.service.IOptionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.rsosor.app.model.support.RsosoRConst.ONE_TIME_TOKEN_HEADER_NAME;
import static com.rsosor.app.model.support.RsosoRConst.ONE_TIME_TOKEN_QUERY_NAME;

/**
 * AbstractAuthenticationFilter
 *
 * @author RsosoR
 * @date 2021/9/26
 */
@Slf4j
public abstract class AbstractAuthenticationFilter extends OncePerRequestFilter {

    protected final AntPathMatcher antPathMatcher;
    protected final RsosoRProperties rsosorProperties;
    protected final IOptionService optionService;
    protected final AbstractStringCacheStore cacheStore;
    private final UrlPathHelper urlPathHelper = new UrlPathHelper();
    private final IOneTimeTokenService oneTimeTokenService;

    private volatile IAuthenticationFailureHandler failureHandler;
    /**
     * Exclude url patterns.
     */
    private Set<String> excludeUrlPatterns = new HashSet<>(16);

    private Set<String> urlPatterns = new LinkedHashSet<>();

    AbstractAuthenticationFilter(RsosoRProperties rsosorProperties,
                                 IOptionService optionService,
                                 AbstractStringCacheStore cacheStore,
                                 IOneTimeTokenService oneTimeTokenService) {
        this.rsosorProperties = rsosorProperties;
        this.optionService = optionService;
        this.cacheStore = cacheStore;
        this.oneTimeTokenService = oneTimeTokenService;

        antPathMatcher = new AntPathMatcher();
    }

    /**
     * Gets token from request.
     *
     * @param request http servlet request must not be null
     * @return token or null
     */
    @Nullable
    protected abstract String getTokenFromRequest(@NonNull HttpServletRequest request);

    /**
     * Gets token from http servlet request.
     *
     * @param request http servlet request must not be null
     * @param tokenQueryName token query name must not be blank
     * @param tokenHeaderName token header name must not be blank
     * @return corresponding token
     */
    protected String getTokenFromRequest(@NonNull HttpServletRequest request,
                                         @NonNull String tokenQueryName, @NonNull String tokenHeaderName) {
        Assert.notNull(request, "Http servlet request must not be null");
        Assert.hasText(tokenQueryName, "Token query name must not be blank");
        Assert.hasText(tokenHeaderName, "Token header name must not be blank");

        // Get from header
        String accessKey = request.getHeader(tokenHeaderName);

        // Get from param
        if (StringUtils.isBlank(accessKey)) {
            accessKey = request.getParameter(tokenQueryName);
            log.debug("Got access key from parameter: [{}: {}]", tokenQueryName, accessKey);
        } else {
            log.debug("Got access key from header: [{}: {}]", tokenHeaderName, accessKey);
        }

        return accessKey;
    }

    protected abstract void doAuthenticate(HttpServletRequest request, HttpServletResponse response,
                                           FilterChain filterChain) throws ServletException, IOException;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        Assert.notNull(request, "Http servlet request must not be null");

        // check white list
        boolean result = excludeUrlPatterns.stream()
                .anyMatch(p -> antPathMatcher.match(p, urlPathHelper.getRequestUri(request)));

        return result || urlPatterns.stream()
                .noneMatch(p -> antPathMatcher.match(p, urlPathHelper.getRequestUri(request)));
    }

    /**
     * Adds exclude url patterns.
     *
     * @param excludeUrlPatterns exclude urls
     */
    public void addExcludeUrlPatterns(@NonNull String... excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "Exclude url patterns must not be null");

        Collections.addAll(this.excludeUrlPatterns, excludeUrlPatterns);
    }

    /**
     * Gets exclude url patterns.
     *
     * @return exclude url patterns.
     */
    @NonNull
    public Set<String> getExcludeUrlPatterns() {
        return excludeUrlPatterns;
    }

    /**
     * Sets exclude url patterns.
     *
     * @param excludeUrlPatterns exclude urls
     */
    public void setExcludeUrlPatterns(@NonNull Collection<String> excludeUrlPatterns) {
        Assert.notNull(excludeUrlPatterns, "Exclude url patterns must not be null");

        this.excludeUrlPatterns = new HashSet<>(excludeUrlPatterns);
    }

    public Collection<String> getUrlPatterns() {
        return this.urlPatterns;
    }

    public void setUrlPatterns(Collection<String> urlPatterns) {
        Assert.notNull(urlPatterns, "UrlPatterns must not be null");
        this.urlPatterns = new LinkedHashSet<>(urlPatterns);
    }

    public void addUrlPatterns(String... urlPatterns) {
        Assert.notNull(urlPatterns, "UrlPatterns must not be null");
        Collections.addAll(this.urlPatterns, urlPatterns);
    }

    /**
     * Gets authentication failure handler. (Default: @DefaultAuthenticationFailureHandler)
     *
     * @return authentication failure handler
     */
    @NonNull
    private IAuthenticationFailureHandler getFailureHandler() {
        if (failureHandler == null) {
            synchronized (this) {
                if (failureHandler == null) {
                    // Create default authentication failure handler
                    DefaultAuthenticationFailureHandler failureHandler =
                            new DefaultAuthenticationFailureHandler();
                    failureHandler.setProductionEnv(rsosorProperties.getMode().isProductionEnv());

                    this.failureHandler = failureHandler;
                }
            }
        }
        return failureHandler;
    }

    /**
     * Sets authentication failure handler.
     *
     * @param failureHandler authentication failure handler
     */
    public synchronized void setFailureHandler(
            @NonNull IAuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "Authentication failure handler must not be null");

        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Check whether the blog is installed or not
        Boolean isInstalled =
                optionService
                        .getByPropertyOrDefault(PrimaryProperties.IS_INSTALLED, Boolean.class, false);

        if (!isInstalled && !Mode.TEST.equals(rsosorProperties.getMode())) {
            // If not installed
            getFailureHandler().onFailure(request, response, new NotInstallException("???????????????????????????"));
            return;
        }

        try {
            // Check the one-time-token
            if (isSufficientOneTimeToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            // Do authenticate
            doAuthenticate(request, response, filterChain);
        } catch (AbstractRsosoRException e) {
            getFailureHandler().onFailure(request, response, e);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * Check if the sufficient one-time token is set.
     *
     * @param request http servlet request
     * @return true if sufficient; false otherwise
     */
    private boolean isSufficientOneTimeToken(HttpServletRequest request) {
        // Check the param
        final String oneTimeToken =
                getTokenFromRequest(request, ONE_TIME_TOKEN_QUERY_NAME, ONE_TIME_TOKEN_HEADER_NAME);

        if (StringUtils.isBlank(oneTimeToken)) {
            // If no one-time token is not provided, skip
            return false;
        }

        // Get allowed uri
        String allowedUri = oneTimeTokenService.get(oneTimeToken)
                .orElseThrow(() -> new BadRequestException(
                        "The one-time token does not exist or has been expired")
                        .setErrorData(oneTimeToken));

        // Get request uri
        String requestUri = request.getRequestURI();

        if (!StringUtils.equals(requestUri, allowedUri)) {
            // If the request uri mismatches the allowed uri
            // TODO using ant path matcher could be better
            throw new ForbiddenException("The one-time token does not correspond the request uri")
                    .setErrorData(oneTimeToken);
        }

        // Revoke the token before return
        oneTimeTokenService.revoke(oneTimeToken);

        return true;
    }
}
