package xyz.jdynb.service;

import xyz.jdynb.dto.UserAuthDTO;
import xyz.jdynb.entity.User;

public interface UserService {

    User getByUsername(String username);

    void add(UserAuthDTO userAuthDTO);

    User getByUsernameAndPwd(UserAuthDTO userAuthDTO);

    User getById(Long userId);
}
