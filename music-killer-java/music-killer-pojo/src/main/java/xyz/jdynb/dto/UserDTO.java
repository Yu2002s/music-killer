package xyz.jdynb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 最后 ip 位置
     */
    private String lastIp;

}
