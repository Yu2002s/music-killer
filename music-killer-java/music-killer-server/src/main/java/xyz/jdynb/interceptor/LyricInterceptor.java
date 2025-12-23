package xyz.jdynb.interceptor;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LyricInterceptor extends BasePathMatchInterceptor {

    @Override
    protected Response doIntercept(Chain chain) throws IOException {

        return null;
    }
}
