package xyz.jdynb.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询歌单所需的参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Schema(description = "查询歌单参数")
public class QueryPlaylistParams extends PageParams {

    @Schema(description = "查询id类型", example = "1,rcm")
    private String id;

    @Schema(description = "歌单id", example = "1")
    private String pid;

    @Schema(description = "排序", example = "new,hot", nullable = true)
    private String order;
}
