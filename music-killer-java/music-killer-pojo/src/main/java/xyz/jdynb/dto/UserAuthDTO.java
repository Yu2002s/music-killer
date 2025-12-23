package xyz.jdynb.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Schema(description = "用户认证")
@Data
public class UserAuthDTO implements Serializable {

    @Schema(description = "用户名", example = "test")
    @NotNull(message = "用户名不能为空")
    @Length(min = 2, max = 12, message = "用户名格式在2到12位")
    private String username;

    @Schema(description = "密码", example = "123456")
    @NotNull(message = "密码不能为空")
    @Length(min = 5, max = 18, message = "密码格式在5到18位")
    private String password;
}
