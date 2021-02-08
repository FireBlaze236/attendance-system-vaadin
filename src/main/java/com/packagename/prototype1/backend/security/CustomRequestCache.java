package com.packagename.prototype1.backend.security;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Custom Http request handler to handle internal requests
 */
class CustomRequestCache extends HttpSessionRequestCache {

    /**
     * Special request handler to handle Vaadin internal requests apart from normal requests to bypass Spring Security.
     * @param request
     * @param response
     */
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }

}