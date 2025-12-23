package xyz.jdynb.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.Accessors;
import lombok.Data;

@Schema(description = "播放信息")
@Data
@Accessors(chain = true)
public class PlayInfo {

    @Schema(description = "码率", example = "128k")
    private Integer bitrate;

    @Schema(description = "时长", example = "1")
    private Integer duration;

    private String format;

    @Schema(description = "歌曲id", example = "1")
    @JsonAlias("rid")
    private Long musicId;

    private String sig;

    private String source;

    private Integer type;

    @Schema(description = "播放地址")
    private String url;

    private String user;

}