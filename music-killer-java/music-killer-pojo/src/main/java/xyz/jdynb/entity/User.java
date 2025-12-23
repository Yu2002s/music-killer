package xyz.jdynb.entity;


import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "用户信息")
public class User {
    @Schema(description = "唯一id", example = "1")
    private Long id;
    @Schema(description = "头像", example = "https://image.baidu.com/123.jpg")
    private String avatar;
    @Schema(description = "用户名", example = "12345")
    private String username;
    @Schema(description = "昵称", example = "test")
    private String nickname;
    @Schema(description = "密码", example = "123456")
    private String password;
    @Schema(description = "创建时间", example = "2025-01-01 12:00:00")
    private LocalDateTime createTime;
    @Schema(description = "更新时间", example = "2025-09-10 13:00:00")
    private LocalDateTime updateTime;
    @Schema(description = "最后ip地址", example = "127.0.0.1")
    private String lastIp;
    @Schema(description = "状态:1启用0禁用", example = "1")
    private Integer status;
}
