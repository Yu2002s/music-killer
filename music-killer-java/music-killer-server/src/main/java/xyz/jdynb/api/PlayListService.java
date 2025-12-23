package xyz.jdynb.api;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.jdynb.entity.PlayList;
import xyz.jdynb.entity.PlayListTag;
import xyz.jdynb.interceptor.SignInterceptor;
import xyz.jdynb.data.PageResponse;
import xyz.jdynb.result.PageResult;
import xyz.jdynb.result.Result;

import java.util.List;

/**
 * 歌单服务
 */
@RetrofitClient(baseUrl = "https://www.kuwo.cn/api/www/")
@Intercept(handler = SignInterceptor.class)
public interface PlayListService {

    /**
     * 获取推荐歌单列表
     *
     * @return 推荐歌单列表
     */
    @GET("rcm/index/playlist")
    Result<PageResponse<PlayList>> getRecommendPlayList(@Query("id") String tagId);

    /**
     * 通过标签分类获取歌单列表
     */
    @GET("classify/playlist/getTagPlayList")
    Result<PageResult<PlayList>> getPlayListByTag(@Query("id") String tagId);

    /**
     * 获取首页的歌单的标签
     *
     * @return 歌单标签列表
     */
    @GET("playlist/index/tags")
    Result<List<PlayListTag>> getIndexPlayListTags();

    /**
     * 获取全部的歌单标签列表
     *
     * @return 歌单标签列表（包含子集）
     */
    @GET("playlist/getTagList")
    Result<List<PlayListTag>> getPlayListTags();

    /**
     * 获取分类歌单列表
     *
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param order 排序规则 new、hot
     * @return 歌单列表
     */
    @GET("classify/playlist/getRcmPlayList")
    Result<PageResult<PlayList>> getRcmPlayList(
            @Query("pn") Integer pageNo,
            @Query("rn") Integer pageSize,
            @Query("order") String order
    );

    /**
     * 获取分类的歌单
     *
     * @param tagId 分类标签 id
     * @param pageNo 页码
     * @param pageSize 页大小
     * @param order 排序规则 new、hot
     * @return 歌单列表
     */
    @GET("classify/playlist/getTagPlayList")
    Result<PageResult<PlayList>> getTagPlayList(
            @Query("id") String tagId,
            @Query("pn") Integer pageNo,
            @Query("rn") Integer pageSize,
            @Query("order") String order
    );

    /**
     * 获取歌单详情
     *
     * @param pid      歌单 id
     * @param pageNo   页码
     * @param pageSize 页大小
     * @return 歌单信息
     */
    @GET("playlist/playListInfo")
    Result<PlayList> getPlayListInfo(
            @Query("pid") String pid,
            @Query("pn") Integer pageNo,
            @Query("rn") Integer pageSize
    );
}
