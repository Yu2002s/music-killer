package xyz.jdynb.filter;

import ch.qos.logback.core.util.MD5Util;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.WebFilter;

import java.io.IOException;

/**
 * Api 认证
 */
@Slf4j
@Component
public class ApiAuthFilter implements Filter {

    @Value("${api.auth.salt}")
    private String SALT;

    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (profile.equals("dev")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse  response = (HttpServletResponse) servletResponse;
        String sign = request.getHeader("Sign");
        if (!StringUtils.hasText(sign)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        String timestamp = request.getHeader("Timestamp");
        long timestampLong = Long.parseLong(timestamp);
        if (System.currentTimeMillis() - timestampLong > 10 * 1000L) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<h1 style='font-size: 100px;'>来呀来呀充八万</h1>");
            return;
        }
        String version = request.getHeader("Version");
        String currentSign = DigestUtils.md5DigestAsHex((SALT + timestamp + version).getBytes());

        if (!currentSign.equalsIgnoreCase(sign)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("<h1 style='font-size: 100px;'>来呀来呀充八万</h1>");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
     }
}
