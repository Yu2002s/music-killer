package xyz.jdynb.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jdynb.entity.LiveItem;
import xyz.jdynb.result.Result;
import xyz.jdynb.service.LiveService;

import java.util.List;

@RestController
@RequestMapping("/live")
public class LiveController {

    @Resource
    private LiveService liveService;

    @GetMapping("/list")
    public Result<List<LiveItem>> getLiveItems() {
        return Result.success(liveService.getItems());
    }

}
