package com.myxh.chatgpt.domain.other;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description OpenAi 响应
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class OpenAiResponse<T> implements Serializable
{
    private String object;
    private List<T> data;
    private Error error;

    @Data
    public static class Error
    {
        private String message;
        private String type;
        private String param;
        private String code;
    }
}
