package xyz.jdynb.service.impl;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import xyz.jdynb.entity.LiveItem;
import xyz.jdynb.mapper.LiveMapper;
import xyz.jdynb.service.LiveService;

import java.util.List;

@Service
public class LiveServiceImpl implements LiveService {

    @Resource
    private LiveMapper liveMapper;

    @Override
    public List<LiveItem> getItems() {
        return liveMapper.getItems();
    }
}
