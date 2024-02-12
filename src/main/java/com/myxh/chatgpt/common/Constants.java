package com.myxh.chatgpt.common;

import lombok.Getter;

/**
 * @author MYXH
 * @date 2023/12/11
 * @description 通用类
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
public class Constants
{
    public final static String NULL = "null";

    /**
     * 官网支持的请求角色类型：system、user、assistant
     * https://platform.openai.com/docs/guides/chat/introduction
     */
    @Getter
    public enum Role
    {
        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        ;

        private final String code;

        Role(String code)
        {
            this.code = code;
        }
    }
}
