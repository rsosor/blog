package com.rsosor.app.security.handler;

import com.rsosor.app.exception.AbstractRsosoRException;
import com.rsosor.app.exception.NotInstallException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ContentAuthenticationFailureHandler
 *
 * @author RsosoR
 * @date 2021/9/26
 */
public class ContentAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onFailure(HttpServletRequest request, HttpServletResponse response,
                          AbstractRsosoRException exception) throws IOException, ServletException {
        if (exception instanceof NotInstallException) {
            response.sendRedirect(request.getContextPath() + "/install");
            return;
        }

        // Forward to error
        request.getRequestDispatcher(request.getContextPath() + "/error")
                .forward(request, response);
    }
}
