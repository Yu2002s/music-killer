package xyz.jdynb.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 图片集合
 * @TableName image
 */
@TableName(value ="image")
@Data
public class Image implements Serializable {
    /**
     * 唯一id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "唯一id", example = "1")
    private Integer id;

    /**
     * 图片名称
     */
    @TableField(value = "name")
    @Schema(description = "图片名称", example = "test.jpg")
    private String name;

    /**
     * 图片大小
     */
    @TableField(value = "size")
    @Schema(description = "图片大小", example = "1024")
    private Integer size;

    /**
     * 图片md5值
     */
    @TableField(value = "md5")
    @Schema(description = "图片md5值", example = "e1e1e1e1e1e1e1e1e1e1e1")
    private String md5;

    /**
     * 图片的sha1值
     */
    @TableField(value = "sha1")
    @Schema(description = "图片sha1值")
    private String sha1;

    /**
     * 图片类型
     */
    @TableField(value = "mime")
    @Schema(description = "图片类型", example = "image/jpg")
    private String mime;

    /**
     * 图片真实地址
     */
    @TableField(value = "url")
    @Schema(description = "图片真实地址", example = "https://localhost/test.jpg")
    private String url;

    /**
     * 外部id
     */
    @TableField(value = "ext_id")
    @Schema(description = "外部id：上传服务商产生的", example = "1")
    private Integer ext_id;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    @Schema(description = "创建时间", example = "2025-12-01 12:00:00")
    private LocalDateTime create_time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}