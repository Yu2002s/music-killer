package xyz.jdynb.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "用户信息")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    @Schema(description = "用户id", example = "10001")
    @NotNull
    private Long id;

    @NotNull
    @Schema(description = "用户名(唯一)", example = "test")
    private String username;

    @NotNull
    @Schema(description = "昵称", example = "test")
    private String nickname;

    @NotNull
    @Schema(description = "用户头像", example = "https://image.baidu.com/test.jpg")
    private String avatar;

    @Schema(description = "创建时间", example = "2025-01-01 12:00:00")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", example = "2025-09-10 13:00:00")
    private LocalDateTime updateTime;

}
