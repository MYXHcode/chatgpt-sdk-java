package com.myxh.chatgpt.domain.embedd;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 条目信息
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class Item implements Serializable
{
    private String object;
    private List<BigDecimal> embedding;
    private Integer index;
}
