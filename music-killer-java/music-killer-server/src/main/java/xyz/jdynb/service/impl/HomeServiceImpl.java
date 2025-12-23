package xyz.jdynb.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Service;
import xyz.jdynb.vo.HomeDataVO;
import xyz.jdynb.service.HomeService;
import xyz.jdynb.utils.HtmlUtils;

@Service
@Slf4j
public class HomeServiceImpl implements HomeService {

    @Resource
    private ObjectMapper objectMapper;

    @org.springframework.beans.factory.annotation.Value("${kuwo.url}")
    private String kuwoUrl;

    @Override
    public HomeDataVO getHomeData() {
        try (Context context = Context.newBuilder("js")
                .allowAllAccess(true)
                .build()) {
            Value value = context.eval("js", HtmlUtils.parseNuxtState(kuwoUrl));
            Value data = value.getMember("data");
            if (data.hasArrayElements()) {
                Value stringify = context.eval("js", "JSON.stringify");
                String json = stringify.execute(data.getArrayElement(0), null, 2).asString();
                return objectMapper.readValue(json, HomeDataVO.class);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return new HomeDataVO();
    }
}
