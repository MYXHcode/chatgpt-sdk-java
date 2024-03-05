package com.myxh.chatgpt.session;

import com.myxh.chatgpt.IOpenAiApi;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 配置信息
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Getter
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Configuration
{
    @Setter
    private IOpenAiApi openAiApi;

    @Setter
    private OkHttpClient okHttpClient;

    @NotNull
    private String apiKey;

    private String apiHost;

    /**
     * 字段废弃，不再使用
     */
    @Deprecated
    private String authToken;

    public EventSource.Factory createRequestFactory()
    {
        return EventSources.createFactory(okHttpClient);
    }
}
