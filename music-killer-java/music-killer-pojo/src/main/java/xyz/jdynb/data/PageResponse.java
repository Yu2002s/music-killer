package xyz.jdynb.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {

    /**
     * 分页数据
     */
    @JsonAlias("list")
    private List<T> data;

    private Long total;
}
