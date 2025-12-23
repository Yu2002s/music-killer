package xyz.jdynb.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;

import lombok.Data;

@Data
@Accessors(chain = true)
@Schema(description = "歌曲信息")
public class Music {

    @JsonAlias("ARTIST")
    private String artist;

    @Schema(description = "封面图片", example = "https://image.baidu.com/test.jpg")
    private String pic;

    @JsonAlias("web_artistpic_short")
    private String artistPic;

    @JsonAlias({"rid", "DC_TARGETID"})
    @Schema(description = "歌曲id", example = "1")
    private Long id;

    @Schema(description = "歌曲时长,单位秒", example = "12")
    @JsonAlias("DURATION")
    private Integer duration;

    @Schema(description = "是否有无损音质", example = "false")
    private Boolean hasLossless;

    @JsonAlias({"hasmv", "MVFLAG"})
    private Integer hasMv;

    @Schema(description = "发行时间")
    @JsonAlias({"releaseDate", "releasedate"})
    private String releaseDate;

    @JsonAlias("ALBUM")
    @Schema(description = "专辑名称", example = "青春派")
    private String album;

    @Schema(description = "专辑id", example = "1")
    @JsonAlias({"albumid", "ALBUMID"})
    private Long albumId;

    @JsonAlias({"artistid", "ARTISTID"})
    private Long artistId;

    @JsonAlias({"albumpic", "web_albumpic_short"})
    private String albumPic;

    @Schema(description = "120px封面图片")
    private String pic120;

    @JsonAlias("NAME")
    @Schema(description = "歌曲名称", example = "我的天空")
    private String name;

    @Schema(description = "歌曲展示名称", example = "我的天空(cover)")
    @JsonAlias("SONGNAME")
    private String fullName;
}