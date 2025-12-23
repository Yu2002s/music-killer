package xyz.jdynb.api;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitClient;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.Intercept;
import retrofit2.http.GET;
import retrofit2.http.Query;
import xyz.jdynb.interceptor.SignInterceptor;

@RetrofitClient(baseUrl = "https://www.kuwo.cn")
@Intercept(handler = SignInterceptor.class)
public interface LyricService {

    @GET("newh5/singles/songinfoandlrc")
    String getLrc(@Query("musicId") Long rid);

}
