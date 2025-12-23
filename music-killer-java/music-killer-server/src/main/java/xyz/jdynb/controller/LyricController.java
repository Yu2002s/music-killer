package xyz.jdynb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import xyz.jdynb.api.LyricService;
import xyz.jdynb.result.Result;

@RestController
@RequestMapping("/lyric")
@Tag(name = "歌词")
public class LyricController {

    @Resource
    private LyricService lyricService;

    @Operation(summary = "获取歌词数据", description = "通过歌曲id获取到歌词数据")
    @GetMapping("/{id}")
    public Result<String> getLyric(@Parameter(description = "歌曲id", required = true, example = "1")
                                   @PathVariable Long id) {
        return Result.success(lyricService.getLrc(id));
    }

}
