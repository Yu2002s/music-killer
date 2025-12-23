package xyz.jdynb.mapper;

import org.apache.ibatis.annotations.Mapper;
import xyz.jdynb.entity.Image;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Yu2002s
* @description 针对表【image(图片集合)】的数据库操作Mapper
* @createDate 2025-10-31 14:46:36
* @Entity xyz.jdynb.entity.Image
*/
@Mapper
public interface ImageMapper extends BaseMapper<Image> {

}




