package xyz.jdynb.controller;

import cn.hutool.core.bean.BeanUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.jdynb.annotation.RateLimit;
import xyz.jdynb.api.SearchService;
import xyz.jdynb.dto.QueryMusicParams;
import xyz.jdynb.entity.Music;
import xyz.jdynb.enums.RateLimitType;
import xyz.jdynb.result.Result;
import xyz.jdynb.vo.SearchAlbumResultVO;
import xyz.jdynb.vo.SearchArtistResultVO;
import xyz.jdynb.vo.SearchMusicResultVO;
import xyz.jdynb.vo.SearchPlaylistResultVO;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/search")
@Tag(name = "搜索")
public class SearchController {

    @Value("${kuwo.album-host}")
    private String albumHost;

    @Value("${kuwo.artist-host}")
    private String artistHost;

    @Resource
    private SearchService searchService;

    @GetMapping("/key")
    @Operation(summary = "获取搜索关键字列表", description = "60秒只能调用120次")
    @RateLimit(limitType = RateLimitType.IP, count = 180)
    public Result<String[]> getKeyList(@Parameter(description = "关键字", example = "hello")
                                       @RequestParam(required = false) String key) {
        return searchService.getSearchKey(key);
    }

    @GetMapping
    @Operation(summary = "根据关键字搜索歌曲")
    @RateLimit(limitType = RateLimitType.IP, count = 30)
    public Result<SearchMusicResultVO> searchMusicByKeyword(QueryMusicParams params) {
        params.setPageNo(params.getPageNo() - 1); // 减一才是真实的
        Map<String, Object> map = BeanUtil.beanToMap(params, false, true);

        map.put("all", params.getKeyword()); // 搜索关键字
        map.put("pn", params.getPageNo());
        map.put("rn", params.getPageSize());
        map.remove("pageNo");
        map.remove("pageSize");
        map.remove("keyword");
        map.put("vipver", 1);
        map.put("client", "kt");
        map.put("ft", "music");
        map.put("cluster", 0);
        map.put("strategy", 2012);
        map.put("encoding", "utf8");
        map.put("rformat", "json");
        map.put("mobi", 1);
        map.put("issubtitle", 1);
        map.put("show_copyright_off", 1);
        SearchMusicResultVO resultVO = searchService.searchMusicByKeyWord(map);
        List<Music> absList = resultVO.getData();
        if (absList != null) {
            absList.forEach(music -> {
                music.setArtistPic(artistHost + music.getArtistPic());
                if (StringUtils.hasText(music.getAlbumPic())) {
                    music.setPic(albumHost + music.getAlbumPic());
                    music.setAlbumPic(music.getPic());
                } else if (StringUtils.hasText(music.getArtistPic())) {
                    music.setPic(music.getArtistPic());
                }
                music.setHasLossless(true); // 写死
            });
        }
        return Result.success(resultVO);
    }

    @Operation(summary = "搜索专辑")
    @RateLimit(limitType = RateLimitType.IP, count = 30)
    @GetMapping("/album")
    public Result<SearchAlbumResultVO> searchAlbumByKeyword(@Validated QueryMusicParams params) {
        return searchService.searchAlbumByKeyword(params.getKeyword(), params.getPageNo(), params.getPageSize());
    }

    @Operation(summary = "搜索歌单")
    @RateLimit(limitType = RateLimitType.IP, count = 30)
    @GetMapping("/playlist")
    public Result<SearchPlaylistResultVO> searchPlaylistByKeyword(@Validated QueryMusicParams params) {
        return searchService.searchPlaylistByKeyword(params.getKeyword(), params.getPageNo(), params.getPageSize());
    }

    @Operation(summary = "搜索歌手")
    @RateLimit(limitType = RateLimitType.IP, count = 30)
    @GetMapping("/artist")
    public Result<SearchArtistResultVO> searchArtistByKeyword(@Validated QueryMusicParams params) {
        return searchService.searchArtistByKeyword(params.getKeyword(), params.getPageNo(), params.getPageSize());
    }
}
