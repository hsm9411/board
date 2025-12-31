package com.spring.common.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleCORSFilter implements Filter {

    private String[] allowedOrigins;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Load allowed origins from Docker Environment Variable
        String allowedOriginsStr = System.getenv("APP_CORS_ALLOWED_ORIGINS");
        if (allowedOriginsStr != null && !allowedOriginsStr.isEmpty()) {
            this.allowedOrigins = allowedOriginsStr.split(",");
        } else {
            this.allowedOrigins = new String[0];
        }
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String origin = request.getHeader("Origin");

        if (isOriginAllowed(origin)) {
            response.setHeader("Access-Control-Allow-Origin", origin);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, HEAD");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Authorization");
            response.setHeader("Access-Control-Allow-Credentials", "true");
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {}

    private boolean isOriginAllowed(String origin) {
        if (origin == null || this.allowedOrigins.length == 0) {
            return false;
        }
        for (String allowedOrigin : this.allowedOrigins) {
            if (origin.equals(allowedOrigin.trim())) {
                return true;
            }
        }
        return false;
    }
}
