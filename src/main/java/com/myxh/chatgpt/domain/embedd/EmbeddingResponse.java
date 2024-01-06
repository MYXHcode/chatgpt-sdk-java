package com.myxh.chatgpt.domain.embedd;

import com.myxh.chatgpt.domain.other.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 反馈对象
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class EmbeddingResponse implements Serializable
{
    private String object;
    private List<Item> data;
    private String model;
    private Usage usage;
}
