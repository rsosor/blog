package com.rsosor.app.security.context;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * SecurityContextHolder
 *
 * @author RsosoR
 * @date 2021/9/25
 */
public class SecurityContextHolder {

    private static final ThreadLocal<ISecurityContext> CONTEXT_HOLDER = new ThreadLocal<>();

    private SecurityContextHolder() {
    }

    /**
     * Gets context.
     *
     * @return security context
     */
    @NonNull
    public static ISecurityContext getContext() {
        // Get from thread local
        ISecurityContext context = CONTEXT_HOLDER.get();
        if (context == null) {
            // If no context is available now then create an empty context
            context = createEmptyContext();
            // Set to thread local
            CONTEXT_HOLDER.set(context);
        }

        return context;
    }

    /**
     * Sets security context.
     *
     * @param context security context
     */
    public static void setContext(@Nullable ISecurityContext context) {
        CONTEXT_HOLDER.set(context);
    }

    /**
     * Clears context.
     */
    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    /**
     * Creates an empty security context.
     *
     * @return an empty security context
     */
    @NonNull
    private static ISecurityContext createEmptyContext() {
        return new SecurityContextImpl(null);
    }
}
