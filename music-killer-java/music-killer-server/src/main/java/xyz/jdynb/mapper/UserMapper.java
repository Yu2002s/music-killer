package xyz.jdynb.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.jdynb.dto.UserAuthDTO;
import xyz.jdynb.dto.UserDTO;
import xyz.jdynb.entity.User;

@Mapper
public interface UserMapper {

    void add(UserDTO userDTO);

    User getByUsername(String username);

    User getByUsernameAndPwd(UserAuthDTO userAuthDTO);

    User getById(Long userId);
}
