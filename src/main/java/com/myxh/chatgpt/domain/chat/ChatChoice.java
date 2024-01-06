package com.myxh.chatgpt.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 对话信息
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class ChatChoice implements Serializable
{
    private long index;

    /**
     * stream = true 请求参数里返回的属性是 delta
     * stream = false 请求参数里返回的属性是 delta
     */
    @JsonProperty("delta")
    private Message delta;

    @JsonProperty("message")
    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;
}
