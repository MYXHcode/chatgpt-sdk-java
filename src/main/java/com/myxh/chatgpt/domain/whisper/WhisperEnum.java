package com.myxh.chatgpt.domain.whisper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author MYXH
 * @date 2024/1/6
 * @description 请求信息：语音转文字
 * @GitHub <a href="https://github.com/MYXHcode">MYXHcode</a>
 */
@Data
public class WhisperEnum implements Serializable
{
    @Getter
    @AllArgsConstructor
    public enum Model
    {
        WHISPER_1("whisper-1"),
        ;

        private final String code;
    }

    @Getter
    @AllArgsConstructor
    public enum ResponseFormat
    {
        JSON("json"),
        TEXT("text"),
        SRT("srt"),
        VERBOSE_JSON("verbose_json"),
        VTT("vtt"),
        ;

        private final String code;
    }
}
