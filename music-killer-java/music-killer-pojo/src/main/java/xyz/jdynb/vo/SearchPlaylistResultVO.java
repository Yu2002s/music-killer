package xyz.jdynb.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import xyz.jdynb.entity.PlayList;

import java.util.List;

@Data
public class SearchPlaylistResultVO {

    private Integer total;

    @JsonAlias("list")
    private List<PlayList> data;

}
