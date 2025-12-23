package xyz.jdynb.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jdynb.api.RankService;
import xyz.jdynb.dto.QueryRankMusicParams;
import xyz.jdynb.entity.MusicRank;
import xyz.jdynb.entity.RankMenu;
import xyz.jdynb.result.Result;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rank")
@Tag(name = "排行榜")
@Slf4j
public class RankController {

    @Resource
    private RankService rankService;

    @Operation(summary = "获取首页音乐排行榜")
    @GetMapping("/index")
    public Result<List<MusicRank>> getIndexRankList() {
        return rankService.getIndexRankList();
    }

    @Operation(summary = "获取排行榜菜单")
    @GetMapping("/menu")
    public Result<List<RankMenu>> getRankMenu() {
        return rankService.getRankMenu();
    }

    @Operation(summary = "获取排行榜歌曲列表")
    @GetMapping("/getMusicList")
    public Result<MusicRank> getRankMusicList(@Validated QueryRankMusicParams params) {
        return rankService.getRankMusicList(params.getRankId(), params.getPageNo(), params.getPageSize());
    }
}
