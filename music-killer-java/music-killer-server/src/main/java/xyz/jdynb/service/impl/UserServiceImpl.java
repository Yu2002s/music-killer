package xyz.jdynb.service.impl;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import xyz.jdynb.dto.UserAuthDTO;
import xyz.jdynb.dto.UserDTO;
import xyz.jdynb.entity.User;
import xyz.jdynb.exception.BusinessException;
import xyz.jdynb.mapper.UserMapper;
import xyz.jdynb.service.UserService;
import xyz.jdynb.utils.IPUtils;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    public void add(UserAuthDTO userAuthDTO) {
        String username = userAuthDTO.getUsername();
        String password = userAuthDTO.getPassword();
        User user = getByUsername(username);
        if (user != null) {
            throw new BusinessException("用户已存在");
        }
        String pwdMd5 = DigestUtils.md5DigestAsHex(password.getBytes());

        userMapper.add(UserDTO.builder()
                .username(username)
                .nickname(username)
                .password(pwdMd5)
                .lastIp(IPUtils.getIpAddress(request))
                .build());
    }

    @Override
    public User getById(Long userId) {
        return userMapper.getById(userId);
    }

    @Override
    public User getByUsername(String username) {
        return userMapper.getByUsername(username);
    }

    @Override
    public User getByUsernameAndPwd(UserAuthDTO userAuthDTO) {
        userAuthDTO.setPassword(DigestUtils.md5DigestAsHex(userAuthDTO.getPassword().getBytes()));
        return userMapper.getByUsernameAndPwd(userAuthDTO);
    }
}
