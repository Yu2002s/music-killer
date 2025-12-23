package xyz.jdynb.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import xyz.jdynb.entity.Album;

import java.util.List;

@Data
public class SearchAlbumResultVO {

    private Integer total;

    @JsonAlias("albumList")
    private List<Album> data;

}
