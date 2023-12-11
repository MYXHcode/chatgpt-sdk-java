package com.myxh.chatgpt.domain.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 使用量
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Getter
public class Usage implements Serializable
{
    /**
     * 提示令牌
     */
    @JsonProperty("prompt_tokens")
    private long promptTokens;

    /**
     * 完成令牌
     */
    @JsonProperty("completion_tokens")
    private long completionTokens;

    /**
     * 总量令牌
     */
    @JsonProperty("total_tokens")
    private long totalTokens;

    public void setPromptTokens(long promptTokens)
    {
        this.promptTokens = promptTokens;
    }

    public void setCompletionTokens(long completionTokens)
    {
        this.completionTokens = completionTokens;
    }

    public void setTotalTokens(long totalTokens)
    {
        this.totalTokens = totalTokens;
    }
}
