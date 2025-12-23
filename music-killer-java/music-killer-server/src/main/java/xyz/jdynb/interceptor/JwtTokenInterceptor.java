package xyz.jdynb.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import xyz.jdynb.annotation.RequireLogin;
import xyz.jdynb.constant.JwtClaimsConstant;
import xyz.jdynb.context.BaseContext;
import xyz.jdynb.properties.JwtProperties;
import xyz.jdynb.utils.JwtUtil;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request  请求
     * @param response 响应
     * @param handler  methodHandler
     * @return true 放行，false 不放行
     */
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        if (handlerMethod.getMethodAnnotation(RequireLogin.class) == null) {
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            BaseContext.setCurrentId(userId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
