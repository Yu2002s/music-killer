package xyz.jdynb.interceptor;

import cn.hutool.core.io.IoUtil;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import jakarta.annotation.Resource;
import okhttp3.Request;
import okhttp3.Response;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Component;
import xyz.jdynb.properties.KuwoProperties;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * 针对请求追加 ReqId 和 Cookie、Secret 信息
 */
@Component
public class SignInterceptor extends BasePathMatchInterceptor {

    @Resource
    private KuwoProperties kuwoProperties;

    @Override
    protected Response doIntercept(Chain chain) throws IOException {
        String secret;
        String reqId;
        try (Context context = Context.newBuilder("js")
                .option("engine.WarnInterpreterOnly", "false")
                .allowAllAccess(true)
                .build()) {
            InputStream inputStream = this.getClass().getResourceAsStream("/static/js/kuwo.js");
            String js = IoUtil.read(inputStream, StandardCharsets.UTF_8);
            context.eval("js", js);
            Value generateSecret = context.getBindings("js").getMember("generateSecret");
            Value generateReqId = context.getBindings("js").getMember("generateReqId");
            Value value = generateSecret.execute(kuwoProperties.getValue(), kuwoProperties.getKey());
            secret = value.asString();
            reqId = generateReqId.execute().asString();
        }

        Request request = chain.request();
        Request newReq = request.newBuilder()
                .url(request.url().newBuilder()
                        .addQueryParameter("reqId", reqId)
                        .build()
                )
                .addHeader("Cookie", kuwoProperties.getCookie())
                .addHeader("Secret", secret)
                .build();
        return chain.proceed(newReq);
    }
}
