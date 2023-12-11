package com.myxh.chatgpt.session;

import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.ChatCompletionResponse;
import com.myxh.chatgpt.domain.qa.QACompletionRequest;
import com.myxh.chatgpt.domain.qa.QACompletionResponse;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description OpenAI 会话接口
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public interface OpenAiSession
{
    /**
     * 文本问答
     *
     * @param qaCompletionRequest 请求信息
     * @return 返回结果
     */
    QACompletionResponse completions(QACompletionRequest qaCompletionRequest);

    /**
     * 文本问答；简单请求
     *
     * @param question 请求信息
     * @return 返回结果
     */
    QACompletionResponse completions(String question);

    /**
     * 默认 GPT-3.5 问答模型
     *
     * @param chatCompletionRequest 请求信息
     * @return 返回结果
     */
    ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest);
}
