package com.myxh.chatgpt.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 自定义拦截器
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public class OpenAiInterceptor implements Interceptor
{
    /**
     * OpenAi apiKey 需要在官网申请
     */
    private final String apiKey;

    /**
     * 访问授权接口的认证 Token
     */
    private final String authToken;

    public OpenAiInterceptor(String apiKey, String authToken)
    {
        this.apiKey = apiKey;
        this.authToken = authToken;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        return chain.proceed(this.auth(apiKey, chain.request()));
    }

    private Request auth(String apiKey, Request original)
    {
        // 设置 Token 信息；如果没有此类限制，是不需要设置的。
        HttpUrl url = original.url().newBuilder()
                .addQueryParameter("token", authToken)
                .build();

        // 创建请求
        return original.newBuilder()
                .url(url)
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
    }
}
