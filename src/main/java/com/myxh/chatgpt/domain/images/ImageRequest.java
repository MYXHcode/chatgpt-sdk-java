package com.myxh.chatgpt.domain.images;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 图片请求
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Slf4j
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ImageRequest extends ImageEnum implements Serializable
{
    /**
     * 模型
     */
    private String model = Model.DALL_E_3.code;

    /**
     * 问题描述
     */
    @NonNull
    private String prompt;

    /**
     * 为每个提示生成的完成次数
     */
    @Builder.Default
    private Integer n = 1;

    /**
     * 图片大小
     */
    @Builder.Default
    private String size = Size.size_1024.getCode();

    /**
     * 图片格式化方式：URL、B64_JSON
     */
    @JsonProperty("response_format")
    @Builder.Default
    private String responseFormat = ResponseFormat.URL.getCode();

    @Getter
    @AllArgsConstructor
    public enum Model
    {
        DALL_E_2("dall-e-2"),
        DALL_E_3("dall-e-3"),
        ;

        private final String code;
    }
}
