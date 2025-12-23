package xyz.jdynb.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import xyz.jdynb.interceptor.JwtTokenInterceptor;
import xyz.jdynb.json.JacksonObjectMapper;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtTokenInterceptor jwtTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/user/**", "/upload/**", "/pollution/**")
                .excludePathPatterns("/user/login", "/user/register");
    }

    /**
     * 扩展 Sping MVC 框架的消息转换器
     * @param converters 转换器集合
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建一个消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        // 需要为消息转换器设置一个对象转换器，对象转换器可以将java对象系列化
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        // 将自己的消息转换器加入到容器中
        converters.add(0, messageConverter);

        // 需要添加这个转换器解决 spring doc 的 bug
        converters.add(0, new ByteArrayHttpMessageConverter());
    }
}
