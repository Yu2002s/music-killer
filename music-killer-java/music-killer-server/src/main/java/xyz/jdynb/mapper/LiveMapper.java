package xyz.jdynb.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import xyz.jdynb.entity.LiveItem;

import java.util.List;

@Mapper
public interface LiveMapper {

    @Select("select * from live_item")
    List<LiveItem> getItems();

}
