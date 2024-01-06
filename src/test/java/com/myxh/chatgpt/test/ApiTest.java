package com.myxh.chatgpt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myxh.chatgpt.common.Constants;
import com.myxh.chatgpt.domain.billing.BillingUsage;
import com.myxh.chatgpt.domain.billing.Subscription;
import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.ChatCompletionResponse;
import com.myxh.chatgpt.domain.chat.Message;
import com.myxh.chatgpt.domain.edits.EditRequest;
import com.myxh.chatgpt.domain.edits.EditResponse;
import com.myxh.chatgpt.domain.embedd.EmbeddingResponse;
import com.myxh.chatgpt.domain.files.DeleteFileResponse;
import com.myxh.chatgpt.domain.files.UploadFileResponse;
import com.myxh.chatgpt.domain.images.ImageEnum;
import com.myxh.chatgpt.domain.images.ImageRequest;
import com.myxh.chatgpt.domain.images.ImageResponse;
import com.myxh.chatgpt.domain.other.OpenAiResponse;
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

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
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
        configuration.setApiHost("https://api.xfg.im/b8b6/");
        configuration.setApiKey("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");

        // 测试时候，需要先获得授权token：http://api.xfg.im:8080/authorize?username=xfg&password=123 - 此地址暂时有效，后续根据课程首页说明获取 token；https://t.zsxq.com/0d3o5FKvc
        configuration.setAuthToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ4ZmciLCJleHAiOjE2ODQ1OTAwMTMsImlhdCI6MTY4NDU4NjQxMywianRpIjoiMTE2ZjAyYjItZGZmNC00Y2IwLTg4YTMtZThiODJkYzEyYjZiIiwidXNlcm5hbWUiOiJ4ZmcifQ.D5-nCkA47n9ZoGY6yz6d03BF7mz9UxPM-sHgzJwVhS4");

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
     * 上下文对话
     */
    @Test
    public void testChatCompletionsContext()
    {
        // 1-1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(new ArrayList<>())
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .user("testUser01")
                .build();

        // 写入请求信息
        chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content("写一个java冒泡排序").build());

        // 1-2. 发起请求
        ChatCompletionResponse chatCompletionResponse01 = openAiSession.completions(chatCompletion);
        log.info("测试结果：{}", chatCompletionResponse01.getChoices());

        // 写入请求信息
        chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content(chatCompletionResponse01.getChoices().get(0).getMessage().getContent()).build());
        chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content("换一种写法").build());

        ChatCompletionResponse chatCompletionResponse02 = openAiSession.completions(chatCompletion);
        log.info("测试结果：{}", chatCompletionResponse02.getChoices());
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

    /**
     * 文本修复
     */
    @Test
    public void testEdit()
    {
        // 文本请求
        EditRequest textRequest = EditRequest.builder()
                .input("码农会锁")
                .instruction("帮我修改错字")
                .model(EditRequest.Model.TEXT_DAVINCI_EDIT_001.getCode()).build();
        EditResponse textResponse = openAiSession.edit(textRequest);
        log.info("测试结果：{}", textResponse);

        // 代码请求
        EditRequest codeRequest = EditRequest.builder()
                // j <= 10 应该修改为 i <= 10
                .input("for (int i = 1; j <= 10; i++) {\n" +
                        "    System.out.println(i);\n" +
                        "}")
                .instruction("这段代码执行时报错，请帮我修改").model(EditRequest.Model.CODE_DAVINCI_EDIT_001.getCode()).build();
        EditResponse codeResponse = openAiSession.edit(codeRequest);
        log.info("测试结果：{}", codeResponse);
    }

    /**
     * 生成图片
     */
    @Test
    public void testGenImages()
    {
        // 方式1，简单调用
        ImageResponse imageResponse01 = openAiSession.genImages("画一个996加班的程序员");
        log.info("测试结果：{}", imageResponse01);

        // 方式2，调参调用
        ImageResponse imageResponse02 = openAiSession.genImages(ImageRequest.builder()
                .prompt("画一个996加班的程序员")
                .size(ImageEnum.Size.size_256.getCode())
                .responseFormat(ImageEnum.ResponseFormat.B64_JSON.getCode()).build());
        log.info("测试结果：{}", imageResponse02);
    }

    /**
     * 修改图片，有3个方法，入参不同。
     */
    @Test
    public void testEditImages()
    {
        ImageResponse imageResponse = openAiSession.editImages(new File("D:\\CodeProjects\\Java\\ChatGPT\\chatgpt-sdk-java\\docs\\images\\996.png"), "去除图片中的文字");
        log.info("测试结果：{}", imageResponse);
    }

    @Test
    public void testEmbeddings()
    {
        EmbeddingResponse embeddingResponse = openAiSession.embeddings("哈喽", "嗨", "hi!");
        log.info("测试结果：{}", embeddingResponse);
    }

    @Test
    public void testFiles()
    {
        OpenAiResponse<File> openAiResponse = openAiSession.files();
        log.info("测试结果：{}", openAiResponse);
    }

    @Test
    public void testUploadFile()
    {
        UploadFileResponse uploadFileResponse = openAiSession.uploadFile(new File("D:\\CodeProjects\\Java\\ChatGPT\\chatgpt-sdk-java\\docs\\images\\996.png"));
        log.info("测试结果：{}", uploadFileResponse);
    }

    @Test
    public void testDeleteFile()
    {
        DeleteFileResponse deleteFileResponse = openAiSession.deleteFile("file id 上传后才能获得");
        log.info("测试结果：{}", deleteFileResponse);
    }

    @Test
    public void testSubscription()
    {
        Subscription subscription = openAiSession.subscription();
        log.info("测试结果：{}", subscription);
    }

    @Test
    public void testBillingUsage()
    {
        BillingUsage billingUsage = openAiSession.billingUsage(LocalDate.of(2023, 3, 20), LocalDate.now());
        log.info("测试结果：{}", billingUsage.getTotalUsage());
    }
}
