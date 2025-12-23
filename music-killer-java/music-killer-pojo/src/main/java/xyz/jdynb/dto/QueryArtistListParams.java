package xyz.jdynb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryArtistListParams extends PageParams {

    @Schema(description = "分类", example = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer category;

    @Schema(description = "首字母", example = "A", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String prefix;
}
