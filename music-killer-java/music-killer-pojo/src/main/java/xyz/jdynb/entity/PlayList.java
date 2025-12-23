package xyz.jdynb.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.experimental.Accessors;
import lombok.Data;

import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "歌单")
public class PlayList {

    @Schema(description = "歌单封面地址", example = "https://example.cn/1.jpg")
    private String img;

    @Schema(description = "歌单封面地址(700px)", example = "https://example.cn/2.jpg")
    private String img700;

    @Schema(description = "用户名")
    @NotNull
    private String uname;

    // @Schema(description = "用户名", nullable = true)
    // private String userName;

    @Schema(description = "歌曲数量")
    private Integer total;

    @Schema(description = "歌单名称")
    private String name;

    @Schema(description = "听歌量")
    private Long listencnt;

    @Schema(description = "唯一id")
    private Long id;

    private String desc;

    @Schema(description = "描述信息")
    private String info;

    @Schema(description = "所属标签")
    private String tag;

    @Schema(description = "歌曲列表")
    private List<Music> musicList;

}