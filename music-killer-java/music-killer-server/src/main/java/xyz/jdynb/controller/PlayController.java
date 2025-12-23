package xyz.jdynb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jdynb.api.PlayService;
import xyz.jdynb.entity.PlayInfo;
import xyz.jdynb.properties.KuwoProperties;
import xyz.jdynb.result.Result;

import java.util.Map;

/**
 * 播放相关
 */
@RestController
@RequestMapping("/play")
@Tag(name = "播放")
public class PlayController {

    @Resource
    private PlayService playService;

    @Resource
    private KuwoProperties kuwoProperties;

    @GetMapping("/info")
    @Operation(summary = "获取音乐播放信息")
    public Result<PlayInfo> getPlayInfo(@Parameter(description = "音乐id", example = "1", required = true) String id,
                                        @Parameter(description = "码率", example = "2000kflac", required = true) String bridge) {
        Map<String, String> play = kuwoProperties.getPlay();
        play.put("rid", id);
        play.put("br", bridge);
        return playService.getPlayInfo(play);
    }


}
