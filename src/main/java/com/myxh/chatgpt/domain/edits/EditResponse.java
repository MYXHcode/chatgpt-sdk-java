package com.myxh.chatgpt.domain.edits;

import com.myxh.chatgpt.domain.other.Choice;
import com.myxh.chatgpt.domain.other.Usage;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 编辑响应
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class EditResponse implements Serializable
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
    private Choice[] choices;

    /**
     * 创建
     */
    private long created;

    /**
     * 耗材
     */
    private Usage usage;
}
