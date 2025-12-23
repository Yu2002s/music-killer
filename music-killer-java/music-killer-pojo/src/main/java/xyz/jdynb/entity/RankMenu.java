package xyz.jdynb.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.experimental.Accessors;
import lombok.Data;

@Data
@Accessors(chain = true)
public class RankMenu {

    private String name;

    private List<Item> list;

    @Data
    @Accessors(chain = true)
    public static class Item {

        @JsonAlias("sourceid")
        private String sourceId;

        private String intro;

        private String name;

        private String id;

        private String source;

        private String pic;

        private String pub;

    }
}