package com.myxh.chatgpt.domain.whisper;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description Whisper 响应
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class WhisperResponse implements Serializable
{
    private String text;
}
