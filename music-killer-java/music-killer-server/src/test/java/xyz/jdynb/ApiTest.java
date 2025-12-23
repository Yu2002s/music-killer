package xyz.jdynb;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.http.HttpUtil;
import jakarta.annotation.Resource;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.jdynb.properties.KuwoProperties;

import java.io.File;
import java.nio.charset.Charset;

@SpringBootTest
public class ApiTest {

    @Resource
    private KuwoProperties kuwoProperties;

    @Test
    public void testRequest() {
        try (Context context = Context.newBuilder("js").allowAllAccess(true).build()) {

            /*File file = new ClassPathResource("/static/js/3.js").getFile();
            String js = FileUtil.readString(file, Charset.defaultCharset());
            context.eval("js", js);

            Value value = context.getBindings("js").getMember("f");

            Value result = value.execute(kuwoProperties.getValue(), kuwoProperties.getKey());

            System.out.println(result);*/

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
