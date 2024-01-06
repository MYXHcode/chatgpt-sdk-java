package com.myxh.chatgpt;

import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.ChatCompletionResponse;
import com.myxh.chatgpt.domain.qa.QACompletionRequest;
import com.myxh.chatgpt.domain.qa.QACompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 以 ChatGPT 官网 API 模型，定义接口。官网：<a href="https://platform.openai.com/playground">https://platform.openai.com/playground</a>
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public interface IOpenAiApi
{
    String v1_completions = "v1/completions";

    /**
     * 文本问答
     * @param qaCompletionRequest 请求信息
     * @return                    返回结果
     */
    @POST(v1_completions)
    Single<QACompletionResponse> completions(@Body QACompletionRequest qaCompletionRequest);

    String v1_chat_completions = "v1/chat/completions";

    /**
     * 默认 GPT-3.5 问答模型
     * @param chatCompletionRequest 请求信息
     * @return                      返回结果
     */
    @POST(v1_chat_completions)
    Single<ChatCompletionResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);
}
