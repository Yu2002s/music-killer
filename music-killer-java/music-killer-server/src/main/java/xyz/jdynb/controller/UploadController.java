package xyz.jdynb.controller;

import cn.hutool.http.HttpRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.jdynb.annotation.RateLimit;
import xyz.jdynb.annotation.RequireLogin;
import xyz.jdynb.constant.StatusCodeConstant;
import xyz.jdynb.context.BaseContext;
import xyz.jdynb.entity.Image;
import xyz.jdynb.entity.User;
import xyz.jdynb.enums.RateLimitType;
import xyz.jdynb.exception.BusinessException;
import xyz.jdynb.result.ImageResult;
import xyz.jdynb.result.Result;
import xyz.jdynb.service.ImageService;
import xyz.jdynb.service.UserService;

import java.io.File;

@RestController
@RequestMapping("/upload")
@Slf4j
@Tag(name = "上传")
@Validated
public class UploadController {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private UserService userService;

    @Resource
    private ImageService imageService;

    @Value("${upload.url}")
    private String uploadUrl;

    @Value("${upload.token}")
    private String token;

    @PostMapping("/image")
    @RequireLogin
    @CrossOrigin
    @RateLimit(time = 60, count = 20, limitType = RateLimitType.USER, message = "上传图片频繁，请稍后重试")
    @Operation(summary = "上传图片", description = "上传单个图片，有限流，1分钟只能上传20次")
    public Result<Image> uploadImage(@Parameter(description = "图片", required = true)
                                           @NotNull(message = "请选择图片") MultipartFile image) throws Exception {
        User user = userService.getById(BaseContext.getCurrentId());

        if (user.getStatus() != 1) {
            throw new BusinessException("当前账号禁止上传图片");
        }

        String body = HttpRequest.post(uploadUrl)
                .header("token", token)
                .form("image", image.getBytes(), image.getOriginalFilename())
                // .form("folder", "keeper")
                .execute()
                .body();

        Result<ImageResult> result = objectMapper.readValue(body, new TypeReference<>() {
        });

        if (result.getCode() == StatusCodeConstant.SUCCESS) {
            Image imgObj = new Image();
            BeanUtils.copyProperties(result.getData(), imgObj);
            imgObj.setExt_id(imgObj.getId());
            imgObj.setId(null);
            imageService.saveOrUpdate(imgObj);
            return Result.success(imgObj);
        }

        return Result.error("上传图片失败");
    }

    @PostMapping("/file")
    @CrossOrigin
    @Operation(summary = "上传文件", description = "测试上传文件")
    public Result<String> uploadFile(@Parameter(description = "文件", required = true)
                                     @NotNull(message = "请选择文件") MultipartFile file) throws Exception {
        return Result.success();
    }
}
