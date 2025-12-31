package com.spring.common.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleCORSFilter implements Filter {

    // [핵심] 허용할 프론트엔드 서버의 출처(Origin) 목록을 미리 정의합니다.
    private final List<String> allowedOrigins = Arrays.asList(
            "http://localhost:8081", // Vue 개발 서버
            "http://localhost:5173"  // React 개발 서버
            // 향후 운영 서버 배포 시, "http://운영도메인.com" 등을 추가할 수 있습니다.
    );

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 1. 요청 헤더에서 "Origin" 값을 가져옵니다.
        String origin = request.getHeader("Origin");

        // 2. 이 "Origin"이 우리가 허용한 목록(allowedOrigins)에 포함되어 있는지 확인합니다.
        if (allowedOrigins.contains(origin)) {
            // 3. 만약 허용된 Origin이라면, 응답 헤더에 요청을 보낸 바로 그 Origin을 명시해줍니다.
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        // --- 나머지 CORS 헤더 설정 (기존과 동일) ---
        // 로그인 상태 유지를 위한 쿠키 전송을 허용합니다.
        response.setHeader("Access-Control-Allow-Credentials", "true");
        // 허용할 HTTP 메소드들을 지정합니다.
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, PATCH");
        // 사전 요청(Preflight)의 유효 기간을 설정합니다.
        response.setHeader("Access-Control-Max-Age", "3600");
        // 허용할 요청 헤더들을 지정합니다.
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me, Origin, Authorization");

        chain.doFilter(req, res);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}