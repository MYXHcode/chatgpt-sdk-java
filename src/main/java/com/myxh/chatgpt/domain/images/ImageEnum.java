package com.myxh.chatgpt.domain.images;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 图片枚举配置
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public class ImageEnum
{
    @Getter
    @AllArgsConstructor
    public enum Size
    {
        size_256("256x256"),
        size_512("512x512"),
        size_1024("1024x1024"),
        ;

        private final String code;
    }

    @Getter
    @AllArgsConstructor
    public enum ResponseFormat
    {
        URL("url"),
        B64_JSON("b64_json"),
        ;

        private final String code;
    }
}
