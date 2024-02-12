package com.myxh.chatgpt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myxh.chatgpt.common.Constants;
import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.Message;
import com.myxh.chatgpt.session.Configuration;
import com.myxh.chatgpt.session.OpenAiSession;
import com.myxh.chatgpt.session.OpenAiSessionFactory;
import com.myxh.chatgpt.session.defaults.DefaultOpenAiSessionFactory;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 客户端输入测试
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Slf4j
public class ClientTest
{
    public static void main(String[] args) throws InterruptedException, JsonProcessingException
    {
        // 1. 配置文件；
        // 1.1 官网原始 apiHost https://api.openai.com/ - 官网的 Key 可直接使用
        // 1.2 三方公司 apiHost https://pro-share-aws-api.zcyai.com/ - 需要找我获得 Key
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://pro-share-aws-api.zcyai.com/");
        configuration.setApiKey("sk-b0A0eSKTNxgBqrHv7aAa0808EdB849C89499D928648bD416");

        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        OpenAiSession openAiSession = factory.openSession();

        System.out.println("我是 OpenAI ChatGPT，请输入你的问题：");

        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .stream(true)
                .messages(new ArrayList<>())
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .maxTokens(1024)
                .build();

        // 3. 等待输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine())
        {
            String text = scanner.nextLine();

            chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content(text).build());
            EventSource eventSource = openAiSession.chatCompletions(chatCompletion, new EventSourceListener()
            {
                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data)
                {
                    System.out.println(data);
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response)
                {
                    System.out.println(response.code());
                }

                @Override
                public void onClosed(EventSource eventSource)
                {
                    System.out.println("请输入你的问题：");
                }
            });
        }
    }
}
