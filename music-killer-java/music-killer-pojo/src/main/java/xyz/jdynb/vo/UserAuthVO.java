package xyz.jdynb.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "用户认证信息")
public class UserAuthVO {

    /**
     * 用户凭证
     */
    @Schema(description = "用户凭证", example = "ey123131")
    @NotNull
    private String token;

    /**
     * 凭证过期时间
     */
    @Schema(description = "凭证过期时间", example = "1722312312323")
    @NotNull
    private Long expiresTime;
}
