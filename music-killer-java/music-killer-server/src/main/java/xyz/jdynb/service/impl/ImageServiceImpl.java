package xyz.jdynb.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.jdynb.entity.Image;
import xyz.jdynb.service.ImageService;
import xyz.jdynb.mapper.ImageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Yu2002s
* @description 针对表【image(图片集合)】的数据库操作Service实现
* @createDate 2025-10-31 14:46:36
*/
@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image>
    implements ImageService{

}




