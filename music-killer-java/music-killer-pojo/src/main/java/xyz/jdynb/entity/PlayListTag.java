package xyz.jdynb.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "歌单标签")
public class PlayListTag {

    @Schema(description = "标签id", example = "1")
    private String id;

    @Schema(description = "标签名称", example = "网络")
    private String name;

    /**
     * 子集
     */
    @Schema(description = "子标签列表")
    private List<PlayListTag> data;

}
