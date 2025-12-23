package xyz.jdynb.result;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.jdynb.constant.StatusCodeConstant;

import java.io.Serializable;

/**
 * 后端统一返回结果
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "统一返回结果")
public class Result<T> implements Serializable {

    @Schema(description = "状态码：200成功其他失败", example = "200")
    private Integer code; //编码
    @Schema(description = "错误信息", example = "数据获取成功", nullable = true)
    @Nullable
    private String msg; //错误信息
    @Schema(description = "成功数据", nullable = true)
    @Nullable
    private T data; //数据

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = StatusCodeConstant.SUCCESS;
        return result;
    }

    public static <T> Result<T> success(T object) {
        return success(object, null);
    }

    public static <T> Result<T> success(T object, @Nullable String msg) {
        Result<T> result = new Result<>();
        result.data = object;
        result.code = StatusCodeConstant.SUCCESS;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> successMessage(String msg) {
        return new Result<>(StatusCodeConstant.SUCCESS, msg, null);
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = StatusCodeConstant.ERROR;
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }

}
