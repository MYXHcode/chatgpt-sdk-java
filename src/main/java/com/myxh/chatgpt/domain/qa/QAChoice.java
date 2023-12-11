package com.myxh.chatgpt.domain.qa;

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
public class QAChoice implements Serializable
{
    private long index;
    private String text;
    private Object logprobs;

    @JsonProperty("finish_reason")
    private String finishReason;
}
