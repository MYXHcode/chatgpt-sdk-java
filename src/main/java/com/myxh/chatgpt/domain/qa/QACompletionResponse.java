package com.myxh.chatgpt.domain.qa;

import com.myxh.chatgpt.domain.other.Usage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 问答完成响应
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class QACompletionResponse implements Serializable
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
    private QAChoice[] choices;

    /**
     * 创建
     */
    private long created;

    /**
     * 耗材
     */
    private Usage usage;
}
