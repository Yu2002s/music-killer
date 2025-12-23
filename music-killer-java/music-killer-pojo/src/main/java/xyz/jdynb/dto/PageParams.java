package xyz.jdynb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "分页参数")
@Data
public class PageParams {

    @Schema(description = "页码", example = "1")
    @NotNull
    private Integer pageNo;

    @Schema(description = "pageSize", example = "5")
    @NotNull
    private Integer pageSize;

}
