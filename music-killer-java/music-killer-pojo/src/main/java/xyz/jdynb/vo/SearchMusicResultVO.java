package xyz.jdynb.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import xyz.jdynb.entity.Music;

import java.util.List;

@Data
public class SearchMusicResultVO {

    @JsonAlias("TOTAL")
    private Integer total;

    @JsonAlias("abslist")
    private List<Music> data;

}
