package xyz.jdynb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.jdynb.annotation.RateLimit;
import xyz.jdynb.annotation.RequireLogin;
import xyz.jdynb.constant.JwtClaimsConstant;
import xyz.jdynb.context.BaseContext;
import xyz.jdynb.dto.UserAuthDTO;
import xyz.jdynb.entity.User;
import xyz.jdynb.enums.RateLimitType;
import xyz.jdynb.exception.BusinessException;
import xyz.jdynb.properties.JwtProperties;
import xyz.jdynb.result.Result;
import xyz.jdynb.service.UserService;
import xyz.jdynb.utils.JwtUtil;
import xyz.jdynb.vo.UserAuthVO;
import xyz.jdynb.vo.UserVO;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户控制器 - 演示限流注解的使用
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 登录，生成 token 相关信息
     * @param id 用户 id
     * @return 用户凭证信息
     */
    private Result<UserAuthVO> login(Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID, id);

        long millis = jwtProperties.getUserTtl().toMillis();
        long expMillis = System.currentTimeMillis() + millis;

        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), millis, claims);

        return Result.success(new UserAuthVO(token, expMillis), "登录/注册成功");
    }

    /**
     * 登录接口 - 按IP限流，1分钟内最多5次
     */
    @RateLimit(time = 60, count = 5, limitType = RateLimitType.IP, message = "登录请求过于频繁，请稍后再试")
    @PostMapping("/login")
    @Operation(summary = "登录", description = "有ip限流，1分钟只能登录5次")
    public Result<UserAuthVO> login(@Validated @RequestBody UserAuthDTO userAuthDTO) {
        User user = userService.getByUsernameAndPwd(userAuthDTO);
        if (user == null) {
            throw new BusinessException("用户不存在或密码错误");
        }
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        return login(user.getId());
    }

    /**
     * 注册接口 - 按IP限流，1小时内最多3次
     */
    @RateLimit(time = 3600, count = 3, limitType = RateLimitType.IP, message = "注册请求过于频繁，请1小时后再试")
    @PostMapping("/register")
    @Operation(summary = "注册", description = "有ip限流，1小时内只能注册3次")
    public Result<UserAuthVO> register(@Validated @RequestBody UserAuthDTO userAuthDTO) {
        userService.add(userAuthDTO);
        User user = userService.getByUsername(userAuthDTO.getUsername());
        return login(user.getId());
    }

    /**
     * 获取用户信息 - 默认限流，1分钟内最多100次
     */
    @RateLimit(time = 60, count = 100, limitType = RateLimitType.USER)
    @GetMapping("/info")
    @RequireLogin
    @Operation(summary = "获取当前登录用户信息", description = "1分钟只能获取100次")
    public Result<UserVO> getUserInfo() {
        Long userId = BaseContext.getCurrentId();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userService.getById(userId), userVO);
        return Result.success(userVO);
    }

    /**
     * 发送验证码 - 按IP限流，1分钟内最多1次
     */
    @RateLimit(time = 60, count = 1, limitType = RateLimitType.IP, message = "验证码发送过于频繁，请1分钟后再试")
    @PostMapping("/send-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String phone) {
        return ResponseEntity.ok("验证码已发送到: " + phone);
    }

    /**
     * 修改密码 - 按用户ID限流，1小时内最多5次
     */
    @RateLimit(time = 3600, count = 5, limitType = RateLimitType.USER, message = "密码修改过于频繁，请稍后再试")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return ResponseEntity.ok("密码修改成功");
    }

    /**
     * 无限流的测试接口
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("测试接口，无限流限制");
    }
}
