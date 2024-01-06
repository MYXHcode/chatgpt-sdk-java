package com.myxh.chatgpt.domain.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 金额消耗
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class DailyCost
{
    @JsonProperty("timestamp")
    private long timestamp;

    @JsonProperty("line_items")
    private List<LineItem> lineItems;
}
