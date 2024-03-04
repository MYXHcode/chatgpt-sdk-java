package com.myxh.chatgpt.domain.images;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 条目
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class Item implements Serializable
{
    private String url;

    /*
    @JsonProperty("b64_json")
    private String b64Json;
     */

    @JsonProperty("revised_prompt")
    private String revisedPrompt;
}
