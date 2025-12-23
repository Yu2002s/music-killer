package xyz.jdynb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "查询排行榜音乐")
public class QueryRankMusicParams extends PageParams {

    /**
     * 排行榜 id
     */
    @Schema(description = "排行榜id", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String rankId;
}
