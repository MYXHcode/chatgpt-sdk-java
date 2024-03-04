package com.myxh.chatgpt.session.defaults;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myxh.chatgpt.IOpenAiApi;
import com.myxh.chatgpt.common.Constants;
import com.myxh.chatgpt.domain.billing.BillingUsage;
import com.myxh.chatgpt.domain.billing.Subscription;
import com.myxh.chatgpt.domain.chat.ChatChoice;
import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.ChatCompletionResponse;
import com.myxh.chatgpt.domain.chat.Message;
import com.myxh.chatgpt.domain.edits.EditRequest;
import com.myxh.chatgpt.domain.edits.EditResponse;
import com.myxh.chatgpt.domain.embedd.EmbeddingRequest;
import com.myxh.chatgpt.domain.embedd.EmbeddingResponse;
import com.myxh.chatgpt.domain.files.DeleteFileResponse;
import com.myxh.chatgpt.domain.files.UploadFileResponse;
import com.myxh.chatgpt.domain.images.ImageEditRequest;
import com.myxh.chatgpt.domain.images.ImageRequest;
import com.myxh.chatgpt.domain.images.ImageResponse;
import com.myxh.chatgpt.domain.other.OpenAiResponse;
import com.myxh.chatgpt.domain.qa.QACompletionRequest;
import com.myxh.chatgpt.domain.qa.QACompletionResponse;
import com.myxh.chatgpt.domain.whisper.TranscriptionsRequest;
import com.myxh.chatgpt.domain.whisper.TranslationsRequest;
import com.myxh.chatgpt.domain.whisper.WhisperResponse;
import com.myxh.chatgpt.session.Configuration;
import com.myxh.chatgpt.session.OpenAiSession;
import io.reactivex.Single;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 默认 OpenAI 会话
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public class DefaultOpenAiSession implements OpenAiSession
{
    /**
     * 配置信息
     */
    private final Configuration configuration;

    /**
     * OpenAI 接口
     */
    private final IOpenAiApi openAiApi;

    /**
     * 工厂事件
     */
    private final EventSource.Factory factory;

    public DefaultOpenAiSession(Configuration configuration)
    {
        this.configuration = configuration;
        this.openAiApi = configuration.getOpenAiApi();
        this.factory = configuration.createRequestFactory();
    }

    @Override
    public QACompletionResponse completions(QACompletionRequest qaCompletionRequest)
    {
        return this.openAiApi.completions(qaCompletionRequest).blockingGet();
    }

    @Override
    public EventSource completions(QACompletionRequest qaCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException
    {
        // 核心参数校验：不对用户的传参做更改，只返回错误信息
        if (!qaCompletionRequest.isStream())
        {
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 构建请求信息
        Request request = new Request.Builder()
                .url(configuration.getApiHost().concat(IOpenAiApi.v1_completions))
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsString(qaCompletionRequest)))
                .build();

        // 返回事件结果
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public QACompletionResponse completions(String question)
    {
        QACompletionRequest request = QACompletionRequest
                .builder()
                .prompt(question)
                .build();
        Single<QACompletionResponse> completions = this.openAiApi.completions(request);

        return completions.blockingGet();
    }

    @Override
    public ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest)
    {
        return this.openAiApi.completions(chatCompletionRequest).blockingGet();
    }

    @Override
    public EventSource chatCompletions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException
    {
        return chatCompletions(Constants.NULL, Constants.NULL, chatCompletionRequest, eventSourceListener);
    }

    @Override
    public CompletableFuture<String> chatCompletions(ChatCompletionRequest chatCompletionRequest) throws InterruptedException, JsonProcessingException
    {
        // 用于执行异步任务并获取结果
        CompletableFuture<String> future = new CompletableFuture<>();
        StringBuffer dataBuffer = new StringBuffer();

        chatCompletions(chatCompletionRequest, new EventSourceListener()
        {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data)
            {
                if ("[DONE]".equalsIgnoreCase(data))
                {
                    onClosed(eventSource);
                    future.complete(dataBuffer.toString());
                }

                ChatCompletionResponse chatCompletionResponse = JSON.parseObject(data, ChatCompletionResponse.class);
                List<ChatChoice> choices = chatCompletionResponse.getChoices();

                for (ChatChoice chatChoice : choices)
                {
                    Message delta = chatChoice.getDelta();
                    if (Constants.Role.ASSISTANT.getCode().equals(delta.getRole()))
                    {
                        continue;
                    }

                    // 应答完成
                    String finishReason = chatChoice.getFinishReason();

                    if ("stop".equalsIgnoreCase(finishReason))
                    {
                        onClosed(eventSource);

                        return;
                    }

                    // 发送信息
                    try
                    {
                        dataBuffer.append(delta.getContent());
                    }
                    catch (Exception e)
                    {
                        future.completeExceptionally(new RuntimeException("Request closed before completion"));
                    }
                }
            }

            @Override
            public void onClosed(EventSource eventSource)
            {
                future.complete(dataBuffer.toString());
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response)
            {
                future.completeExceptionally(new RuntimeException("Request closed before completion"));
            }
        });

        return future;
    }

    @Override
    public EventSource chatCompletions(String apiHostByUser, String apiKeyByUser, ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException
    {
        // 核心参数校验；不对用户的传参做更改，只返回错误信息
        if (!chatCompletionRequest.isStream())
        {
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 动态设置 Host、Key，便于用户传递自己的信息
        String apiHost = Constants.NULL.equals(apiHostByUser) ? configuration.getApiHost() : apiHostByUser;
        String apiKey = Constants.NULL.equals(apiKeyByUser) ? configuration.getApiKey() : apiKeyByUser;

        // 构建请求信息
        Request request = new Request.Builder()
                // url: https://api.openai.com/v1/chat/completions - 通过 IOpenAiApi 配置的 POST 接口，用这样的方式从统一的地方获取配置信息
                .url(apiHost.concat(IOpenAiApi.v1_chat_completions))
                .addHeader("apiKey", apiKey)

                // 封装请求参数信息，如果使用了 Fastjson 也可以替换 ObjectMapper 转换对象
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsString(chatCompletionRequest)))
                .build();

        // 返回结果信息；EventSource 对象可以取消应答
        return factory.newEventSource(request, eventSourceListener);
    }

    @Override
    public EditResponse edit(EditRequest editRequest)
    {
        return this.openAiApi.edits(editRequest).blockingGet();
    }

    @Override
    public ImageResponse genImages(String prompt)
    {
        ImageRequest imageRequest = ImageRequest.builder().prompt(prompt).build();

        return this.genImages(imageRequest);
    }

    @Override
    public ImageResponse genImages(ImageRequest imageRequest)
    {
        return this.openAiApi.genImages(imageRequest).blockingGet();
    }

    @Override
    public ImageResponse editImages(File image, String prompt)
    {
        ImageEditRequest imageEditRequest = ImageEditRequest.builder().prompt(prompt).build();

        return this.editImages(image, null, imageEditRequest);
    }

    @Override
    public ImageResponse editImages(File image, ImageEditRequest imageEditRequest)
    {
        return this.editImages(image, null, imageEditRequest);
    }

    @Override
    public ImageResponse editImages(File image, File mask, ImageEditRequest imageEditRequest)
    {
        // 1. imageMultipartBody
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), image);
        MultipartBody.Part imageMultipartBody = MultipartBody.Part.createFormData("image", image.getName(), imageBody);

        // 2. maskMultipartBody
        MultipartBody.Part maskMultipartBody = null;

        if (Objects.nonNull(mask))
        {
            RequestBody maskBody = RequestBody.create(MediaType.parse("multipart/form-data"), mask);
            maskMultipartBody = MultipartBody.Part.createFormData("mask", mask.getName(), maskBody);
        }

        // requestBodyMap
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        requestBodyMap.put("prompt", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getPrompt()));
        requestBodyMap.put("n", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getN().toString()));
        requestBodyMap.put("size", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getSize()));
        requestBodyMap.put("response_format", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getResponseFormat()));

        if (!(Objects.isNull(imageEditRequest.getUser()) || imageEditRequest.getUser().isEmpty()))
        {
            requestBodyMap.put("user", RequestBody.create(MediaType.parse("multipart/form-data"), imageEditRequest.getUser()));
        }

        return this.openAiApi.editImages(imageMultipartBody, maskMultipartBody, requestBodyMap).blockingGet();
    }

    @Override
    public EmbeddingResponse embeddings(String input)
    {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().input(new ArrayList<String>()
        {{
            add(input);
        }}).build();

        return this.embeddings(embeddingRequest);
    }

    @Override
    public EmbeddingResponse embeddings(String... inputs)
    {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().input(Arrays.asList(inputs)).build();
        return this.embeddings(embeddingRequest);
    }

    @Override
    public EmbeddingResponse embeddings(List<String> inputs)
    {
        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder().input(inputs).build();

        return this.embeddings(embeddingRequest);
    }

    @Override
    public EmbeddingResponse embeddings(EmbeddingRequest embeddingRequest)
    {
        return this.openAiApi.embeddings(embeddingRequest).blockingGet();
    }

    @Override
    public OpenAiResponse<File> files()
    {
        return this.openAiApi.files().blockingGet();
    }

    @Override
    public UploadFileResponse uploadFile(File file)
    {
        return this.uploadFile("fine-tune", file);
    }

    @Override
    public UploadFileResponse uploadFile(String purpose, File file)
    {
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);
        RequestBody purposeBody = RequestBody.create(MediaType.parse("multipart/form-data"), purpose);

        return this.openAiApi.uploadFile(multipartBody, purposeBody).blockingGet();
    }

    @Override
    public DeleteFileResponse deleteFile(String fileId)
    {
        return this.openAiApi.deleteFile(fileId).blockingGet();
    }

    @Override
    public WhisperResponse speed2TextTranscriptions(File file, TranscriptionsRequest transcriptionsRequest)
    {
        // 1. 语音文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        // 2. 参数封装
        Map<String, RequestBody> requestBodyMap = new HashMap<>();

        if (StrUtil.isNotBlank(transcriptionsRequest.getLanguage()))
        {
            requestBodyMap.put(TranscriptionsRequest.Fields.language, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionsRequest.getLanguage()));
        }

        if (StrUtil.isNotBlank(transcriptionsRequest.getModel()))
        {
            requestBodyMap.put(TranscriptionsRequest.Fields.model, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionsRequest.getModel()));
        }

        if (StrUtil.isNotBlank(transcriptionsRequest.getPrompt()))
        {
            requestBodyMap.put(TranscriptionsRequest.Fields.prompt, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionsRequest.getPrompt()));
        }

        if (StrUtil.isNotBlank(transcriptionsRequest.getResponseFormat()))
        {
            requestBodyMap.put(TranscriptionsRequest.Fields.responseFormat, RequestBody.create(MediaType.parse("multipart/form-data"), transcriptionsRequest.getResponseFormat()));
        }

        requestBodyMap.put(TranscriptionsRequest.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(transcriptionsRequest.getTemperature())));

        return this.openAiApi.speed2TextTranscriptions(multipartBody, requestBodyMap).blockingGet();
    }

    @Override
    public WhisperResponse speed2TextTranslations(File file, TranslationsRequest translationsRequest)
    {
        // 1. 语音文件
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

        // 2. 参数封装
        Map<String, RequestBody> requestBodyMap = new HashMap<>();

        if (StrUtil.isNotBlank(translationsRequest.getModel()))
        {
            requestBodyMap.put(TranslationsRequest.Fields.model, RequestBody.create(MediaType.parse("multipart/form-data"), translationsRequest.getModel()));
        }

        if (StrUtil.isNotBlank(translationsRequest.getPrompt()))
        {
            requestBodyMap.put(TranslationsRequest.Fields.prompt, RequestBody.create(MediaType.parse("multipart/form-data"), translationsRequest.getPrompt()));
        }

        if (StrUtil.isNotBlank(translationsRequest.getResponseFormat()))
        {
            requestBodyMap.put(TranslationsRequest.Fields.responseFormat, RequestBody.create(MediaType.parse("multipart/form-data"), translationsRequest.getResponseFormat()));
        }

        requestBodyMap.put(TranslationsRequest.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(translationsRequest.getTemperature())));
        requestBodyMap.put(TranscriptionsRequest.Fields.temperature, RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(translationsRequest.getTemperature())));

        return this.openAiApi.speed2TextTranscriptions(multipartBody, requestBodyMap).blockingGet();
    }

    @Override
    public Subscription subscription()
    {
        return this.openAiApi.subscription().blockingGet();
    }

    @Override
    public BillingUsage billingUsage(@NotNull LocalDate starDate, @NotNull LocalDate endDate)
    {
        return this.openAiApi.billingUsage(starDate, endDate).blockingGet();
    }
}
