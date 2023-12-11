package com.myxh.chatgpt.domain.chat;

import com.myxh.chatgpt.domain.other.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 对话请求结果信息
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class ChatCompletionResponse implements Serializable
{
    /**
     * ID
     */
    private String id;

    /**
     * 对象
     */
    private String object;

    /**
     * 模型
     */
    private String model;

    /**
     * 对话
     */
    private List<ChatChoice> choices;

    /**
     * 创建
     */
    private long created;

    /**
     * 耗材
     */
    private Usage usage;
}
