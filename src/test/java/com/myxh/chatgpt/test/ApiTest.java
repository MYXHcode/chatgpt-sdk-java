package com.myxh.chatgpt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myxh.chatgpt.common.Constants;
import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.ChatCompletionResponse;
import com.myxh.chatgpt.domain.chat.Message;
import com.myxh.chatgpt.domain.qa.QACompletionRequest;
import com.myxh.chatgpt.domain.qa.QACompletionResponse;
import com.myxh.chatgpt.session.Configuration;
import com.myxh.chatgpt.session.OpenAiSession;
import com.myxh.chatgpt.session.OpenAiSessionFactory;
import com.myxh.chatgpt.session.defaults.DefaultOpenAiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 单元测试
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Slf4j
public class ApiTest
{
    private OpenAiSession openAiSession;

    @Before
    public void testOpenAiSessionFactory()
    {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://api.openai-proxy.com/");
        configuration.setApiKey("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");
        // 测试时候，需要先获得授权token：http://api.xfg.im:8080/authorize?username=xfg&password=123 - 此地址暂时有效，后续根据课程首页说明获取 token；https://t.zsxq.com/0d3o5FKvc
        configuration.setAuthToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZmciLCJleHAiOjE2ODMyODE2NzEsImlhdCI6MTY4MzI3ODA3MSwianRpIjoiMWUzZTkwYjYtY2UyNy00NzNlLTk5ZTYtYWQzMWU1MGVkNWE4IiwidXNlcm5hbWUiOiJ4ZmcifQ.YgQRJ2U5-9uydtd6Wbkg2YatsoX-y8mS_OJ3FdNRaX0");

        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);

        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }

    /**
     * 简单问答模式
     */
    @Test
    public void testQaCompletions() throws JsonProcessingException
    {
        QACompletionResponse response01 = openAiSession.completions("写个java冒泡排序");
        log.info("测试结果：{}", new ObjectMapper().writeValueAsString(response01.getChoices()));
    }

    /**
     * 简单问答模式 * 流式应答
     */
    @Test
    public void testQaCompletionsStream() throws JsonProcessingException, InterruptedException
    {
        // 1. 创建参数
        QACompletionRequest request = QACompletionRequest
                .builder()
                .prompt("写个java冒泡排序")
                .stream(true)
                .build();

        for (int i = 0; i < 1; i++)
        {
            // 2. 发起请求
            EventSource eventSource = openAiSession.completions(request, new EventSourceListener()
            {
                @Override
                public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data)
                {
                    log.info("测试结果：{}", data);
                }
            });
        }

        // 等待
        new CountDownLatch(1).await();
    }

    /**
     * 此对话模型 3.5 接近于官网体验
     */
    @Test
    public void testChatCompletions()
    {
        // 1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("写一个java冒泡排序").build()))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();

        // 2. 发起请求
        ChatCompletionResponse chatCompletionResponse = openAiSession.completions(chatCompletion);

        // 3. 解析结果
        chatCompletionResponse.getChoices().forEach(e -> log.info("测试结果：{}", e.getMessage()));
    }

    /**
     * 此对话模型 3.5 接近于官网体验 & 流式应答
     */
    @Test
    public void testChatCompletionsStream() throws JsonProcessingException, InterruptedException
    {
        // 1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .stream(true)
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("写一个java冒泡排序").build()))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();

        // 2. 发起请求
        EventSource eventSource = openAiSession.chatCompletions(chatCompletion, new EventSourceListener()
        {
            @Override
            public void onEvent(@NotNull EventSource eventSource, String id, String type, @NotNull String data)
            {
                log.info("测试结果：{}", data);
            }
        });

        // 等待
        new CountDownLatch(1).await();
    }
}
