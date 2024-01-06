package com.myxh.chatgpt.domain.billing;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 消耗列表数据
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class LineItem
{
    /**
     * 模型
     */

    private String name;
    /**
     * 金额
     */
    private BigDecimal cost;
}
