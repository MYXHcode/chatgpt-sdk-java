package com.myxh.chatgpt.session.defaults;

import com.myxh.chatgpt.IOpenAiApi;
import com.myxh.chatgpt.interceptor.OpenAiInterceptor;
import com.myxh.chatgpt.session.Configuration;
import com.myxh.chatgpt.session.OpenAiSession;
import com.myxh.chatgpt.session.OpenAiSessionFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description OpenAI API Factory 会话工厂
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public class DefaultOpenAiSessionFactory implements OpenAiSessionFactory
{
    private final Configuration configuration;

    public DefaultOpenAiSessionFactory(Configuration configuration)
    {
        this.configuration = configuration;
    }

    @Override
    public OpenAiSession openSession()
    {
        // 1. 日志配置
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 2. 开启 Http 客户端
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiInterceptor(configuration.getApiKey()))
                .connectTimeout(450, TimeUnit.SECONDS)
                .writeTimeout(450, TimeUnit.SECONDS)
                .readTimeout(450, TimeUnit.SECONDS)
                .build();
        configuration.setOkHttpClient(okHttpClient);

        // 3. 创建 API 服务
        IOpenAiApi openAiApi = new Retrofit.Builder()
                .baseUrl(configuration.getApiHost())
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(IOpenAiApi.class);
        configuration.setOpenAiApi(openAiApi);

        return new DefaultOpenAiSession(configuration);
    }
}
