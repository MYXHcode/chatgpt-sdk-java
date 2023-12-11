package com.myxh.chatgpt.session.defaults;

import com.myxh.chatgpt.IOpenAiApi;
import com.myxh.chatgpt.domain.chat.ChatCompletionRequest;
import com.myxh.chatgpt.domain.chat.ChatCompletionResponse;
import com.myxh.chatgpt.domain.qa.QACompletionRequest;
import com.myxh.chatgpt.domain.qa.QACompletionResponse;
import com.myxh.chatgpt.session.OpenAiSession;
import io.reactivex.Single;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 默认 OpenAI 会话
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public class DefaultOpenAiSession implements OpenAiSession
{
    private final IOpenAiApi openAiApi;

    public DefaultOpenAiSession(IOpenAiApi openAiApi)
    {
        this.openAiApi = openAiApi;
    }

    @Override
    public QACompletionResponse completions(QACompletionRequest qaCompletionRequest)
    {
        return this.openAiApi.completions(qaCompletionRequest).blockingGet();
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
}
