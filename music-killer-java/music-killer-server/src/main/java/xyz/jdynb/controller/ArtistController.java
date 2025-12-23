package xyz.jdynb.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.jdynb.api.ArtistService;
import xyz.jdynb.data.PageResponse;
import xyz.jdynb.dto.QueryArtistListParams;
import xyz.jdynb.dto.QueryArtistMusicParams;
import xyz.jdynb.entity.Artist;
import xyz.jdynb.entity.Music;
import xyz.jdynb.result.PageResult;
import xyz.jdynb.result.Result;
import xyz.jdynb.vo.ArtistPageVO;

@RestController
@RequestMapping("/artist")
@Tag(name = "歌手")
public class ArtistController {

    @Resource
    private ArtistService artistService;

    @GetMapping("/list")
    @Operation(summary = "获取歌手列表")
    public Result<ArtistPageVO> getArtistList(@Validated QueryArtistListParams params) {
        return artistService.getArtistList(params.getCategory(), params.getPrefix(),
                params.getPageNo(), params.getPageSize());
    }

    @GetMapping("/info")
    @Operation(summary = "获取歌手信息")
    public Result<Artist> getArtistInfo(Long artistId) {
        return artistService.getArtistInfo(artistId);
    }

    @GetMapping("/music")
    @Operation(summary = "获取歌手歌曲列表")
    public Result<PageResponse<Music>> getMusicList(@Validated QueryArtistMusicParams params) {
        return artistService.getArtistMusicList(params.getArtistId(), params.getPageNo(), params.getPageSize());
    }
}
