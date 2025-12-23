package xyz.jdynb.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "查询歌曲参数")
public class QueryMusicParams extends PageParams {

    @Schema(description = "关键字", example = "我的天空")
    @NotNull
    private String keyword;
}
