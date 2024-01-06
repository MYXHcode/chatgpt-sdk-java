package com.myxh.chatgpt.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.ChatCompletionResponse;
import com.myxh.chatgpt.domain.qa.QACompletionRequest;
import com.myxh.chatgpt.domain.qa.QACompletionResponse;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description OpenAI 会话接口
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public interface OpenAiSession
{
    /**
     * 文本问答：简单请求
     *
     * @param question 请求信息
     * @return 返回结果
     */
    QACompletionResponse completions(String question);

    /**
     * 文本问答
     *
     * @param qaCompletionRequest 请求信息
     * @return 返回结果
     */
    QACompletionResponse completions(QACompletionRequest qaCompletionRequest);

    /**
     * 文本问答 & 流式反馈
     *
     * @param qaCompletionRequest 请求信息
     * @param eventSourceListener 实现监听；通过监听的 onEvent 方法接收数据
     */
    EventSource completions(QACompletionRequest qaCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    /**
     * 问答模型 GPT-3.5/4.0
     *
     * @param chatCompletionRequest 请求信息
     * @return 返回结果
     */
    ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest);

    /**
     * 问答模型 GPT-3.5/4.0 & 流式反馈
     *
     * @param chatCompletionRequest 请求信息
     * @param eventSourceListener   实现监听；通过监听的 onEvent 方法接收数据
     * @return 返回结果
     */
    EventSource chatCompletions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;
}
