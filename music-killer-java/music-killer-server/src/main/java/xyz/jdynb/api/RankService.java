package xyz.jdynb.api;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import xyz.jdynb.entity.MusicRank;
import xyz.jdynb.entity.RankMenu;
import xyz.jdynb.interceptor.SignInterceptor;
import xyz.jdynb.result.PageResult;
import xyz.jdynb.result.Result;

import java.util.List;
import java.util.Map;

@RetrofitClient(baseUrl = "https://kuwo.cn/api/www/bang/")
@Intercept(handler = SignInterceptor.class)
public interface RankService {

    @GET("index/bangList")
    Result<List<MusicRank>> getIndexRankList();

    /**
     * 获取排行榜的菜单
     * @return 菜单列表
     */
    @GET("bang/bangMenu")
    Result<List<RankMenu>> getRankMenu();

    @GET("bang/musicList")
    Result<MusicRank> getRankMusicList(
            @Query("bangId") String rankId,
            @Query("pn") Integer pageNo,
            @Query("rn") Integer pageSize
    );
}
