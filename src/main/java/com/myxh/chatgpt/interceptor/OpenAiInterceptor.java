package com.myxh.chatgpt.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
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
    private final String apiKeyBySystem;

    public OpenAiInterceptor(String apiKey)
    {
        this.apiKeyBySystem = apiKey;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException
    {
        // 1. 获取原始 Request
        Request original = chain.request();

        // 2. 读取 apiKey，优先使用自己传递的 apiKey
        String apiKeyByUser = original.header("apiKey");
        String apiKey = null == apiKeyByUser ? apiKeyBySystem : apiKeyByUser;

        // 3. 构建 Request
        Request request = original.newBuilder()
                .url(original.url())
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();

        // 4. 返回执行结果
        return chain.proceed(request);
    }
}
