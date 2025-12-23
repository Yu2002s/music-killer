package xyz.jdynb.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "图片数据")
public class ImageResult {

    /**
     * 该图片在系统中的ID
     */
    @Schema(description = "唯一 id", example = "1")
    private Integer id;

    @Schema(description = "图片md5", example = "abc")
    private String md5;
    /**
     * 图片类型
     */
    @Schema(description = "图片类型", example = "image/jpeg")
    private String mime;
    /**
     * 图片名称
     */
    @Schema(description = "图片名称", example = "1.jpg")
    private String name;
    /**
     * 本用户总空间
     */
    @Schema(description = "本用户总空间", example = "1024")
    private String quota;

    @Schema(description = "图片sha1值", example = "abc")
    private String sha1;
    /**
     * 图片大小。外链展示中图片会进行webp格式转换，所以外链并非原图。
     */
    @Schema(description = "图片大小", example = "1024")
    private Integer size;
    /**
     * 图片外链链接，可以直接访问
     */
    @Schema(description = "图片外链地址", example = "https://test.com/1.jpg")
    private String url;
    /**
     * 本用户已使用空间
     */
    @Schema(description = "本用户已使用空间", example = "1024")
    private String useQuota;

}
