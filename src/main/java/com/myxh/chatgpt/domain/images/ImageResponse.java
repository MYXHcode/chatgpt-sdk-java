package com.myxh.chatgpt.domain.images;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 图片响应
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class ImageResponse implements Serializable
{
    /**
     * 条目数据
     */
    private List<Item> data;

    /**
     * 创建时间
     */
    private long created;
}
