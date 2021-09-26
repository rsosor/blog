package com.rsosor.app.security.handler;

import com.rsosor.app.exception.AbstractRsosoRException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * IAuthenticationFailureHandler
 *
 * @author RsosoR
 * @date 2021/9/26
 */
public interface IAuthenticationFailureHandler {

    /**
     * Calls when a user has been unsuccessfully authenticated.
     *
     * @param request http servlet request
     * @param response http servlet response
     * @param exception api exception
     * @throws IOException io exception
     * @throws ServletException service exception
     */
    void onFailure(HttpServletRequest request, HttpServletResponse response,
                   AbstractRsosoRException exception) throws IOException, ServletException;
}
