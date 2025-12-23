package xyz.jdynb.vo;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import xyz.jdynb.entity.Artist;

import java.util.List;

@Data
public class SearchArtistResultVO {

    private Integer total;

    @JsonAlias("artistList")
    private List<Artist> data;

}
