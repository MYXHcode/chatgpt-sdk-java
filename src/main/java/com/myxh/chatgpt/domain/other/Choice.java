package com.myxh.chatgpt.domain.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 对话信息
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class Choice implements Serializable
{
    private long index;
    private String text;
    private Object logprobs;

    @JsonProperty("finish_reason")
    private String finishReason;
}
