package xyz.jdynb.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "kuwo")
@Data
public class KuwoProperties {

    private String url;

    private String cookie;

    private String key;

    private String value;

    private Map<String, String> play;

}
